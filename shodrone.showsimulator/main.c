#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <dirent.h>
#include <signal.h>
#include <sys/wait.h>
#include <string.h>
#include <fcntl.h>
#include <stdarg.h>
#include <pthread.h>
#include <semaphore.h>
#include <sys/mman.h>

#define BUFFER_SIZE 80

typedef struct
{
    short x;
    short y;
    short z;
} Position;

typedef struct
{
    short id;
    size_t pid;
    char status;
} Drone;

Drone *droneArray = NULL;
int drones = 0;

pthread_t threadCollisionDetector;
pthread_t threadReportWriter;

pthread_mutex_t mutex_collision = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond_collision = PTHREAD_COND_INITIALIZER;

typedef struct
{
    int collision_detected;
    Position *pos;
    short *drone_ids;
    int timeStep;
    int active_drones;
    int simulation_running;
    int totalCollisions;
} CollisionEvent;

CollisionEvent *collision;
int shm_fd = -1;

Drone processDrone;

int maximumCollisionsAllowed = 5;
int simulationTime = 0;

sem_t **sem_step_start_array;
sem_t **sem_step_end_array;
sem_t *sem_wait_exec;
sem_t *sem_collision_start;
sem_t *sem_collision_end;
sem_t *sem_report_writer;

void logMessage(const char *message, ...)
{
    va_list args1, args2;
    char messageWithPID[200];
    snprintf(messageWithPID, sizeof(messageWithPID), "[P %d] [T %d] %s", getpid(), gettid(), message);

    va_start(args1, message);
    va_copy(args2, args1);

    vfprintf(stderr, messageWithPID, args1);

    int fdw = open("simulation_output.txt", O_WRONLY | O_CREAT | O_APPEND, 0644);
    if (fdw >= 0)
    {
        vdprintf(fdw, messageWithPID, args2);
        close(fdw);
    }

    va_end(args1);
    va_end(args2);
}

void *collisionDetector(void *arg)
{
    while (1)
    {
        sem_wait(sem_collision_start);
        
        pthread_mutex_lock(&mutex_collision);
        if (!collision->simulation_running || collision->active_drones <= 0)
        {
            collision->simulation_running = 0;
            break;
        }

        collision->collision_detected = 0;
        for (int i = 0; i < drones; i++)
        {
            collision->drone_ids[i] = 0;
        }

        for (int i = 0; i < drones; i++)
        {
            for (int j = i + 1; j < drones; j++)
            {
                if (collision->pos[i].x == collision->pos[j].x &&
                    collision->pos[i].y == collision->pos[j].y &&
                    collision->pos[i].z == collision->pos[j].z)
                {
                    collision->collision_detected = 1;
                    collision->timeStep = simulationTime;
                    collision->drone_ids[i] = 1;
                    collision->drone_ids[j] = 1;
                }
            }
        }

        if (collision->collision_detected)
        {
            pthread_cond_signal(&cond_collision);
            pthread_mutex_unlock(&mutex_collision);
            sem_wait(sem_report_writer);
            if (collision->totalCollisions >= maximumCollisionsAllowed)
            {
                collision->simulation_running = 0;
            }
        }
        else
            pthread_mutex_unlock(&mutex_collision);

        if(!collision->simulation_running)
            break;

        sem_post(sem_collision_end);
    }
    pthread_mutex_unlock(&mutex_collision);
    sem_post(sem_collision_end);
    return NULL;
}

void *writeReportThread(void *arg)
{
    while (collision->simulation_running)
    {

        while (!collision->collision_detected && collision->simulation_running)
        {
            pthread_cond_wait(&cond_collision, &mutex_collision);
        }

        for (int i = 0; i < drones; i++)
        {
            if (collision->drone_ids[i] == 1)
            {
                logMessage("Collision detected at time step %d for drone %d at position (%d, %d, %d)\n",
                           collision->timeStep, i,
                           collision->pos[i].x, collision->pos[i].y, collision->pos[i].z);
                collision->totalCollisions++;
            }
        }

        collision->collision_detected = 0;
        for (int i = 0; i < drones; i++)
        {
            collision->drone_ids[i] = 0;
        }

        sem_post(sem_report_writer);
    }

    if (collision->totalCollisions >= maximumCollisionsAllowed)
    {
        logMessage("Simulation ended due to maximum collisions reached.\n");
        logMessage("Total collisions: %d\n", collision->totalCollisions);
        logMessage("Simulation time: %d time steps.\n", simulationTime);
        logMessage("Simulation result: FAILED\n");
    }
    else
    {
        logMessage("Simulation ended without reaching maximum collisions.\n");
        logMessage("Total collisions: %d\n", collision->totalCollisions);
        logMessage("Simulation time: %d time steps.\n", simulationTime);
        logMessage("Simulation result: SUCCESS\n");
    }
    sem_post(sem_report_writer);
    return NULL;
}

int loadSettings()
{
    FILE *file = fopen("settings.txt", "r");
    if (file == NULL)
    {
        perror("Unable to open settings file.");
        return 1;
    }

    char line[256];
    while (fgets(line, sizeof(line), file))
    {
        if (sscanf(line, "maximumCollisionsAllowed=%d", &maximumCollisionsAllowed) == 1)
        {
            continue;
        }
    }

    fclose(file);
    return 0;
}

int checkNumberOfDrones()
{
    struct dirent *entry;
    int file_count = 0;
    DIR *dir = opendir("drone_paths/");
    if (dir == NULL)
    {
        perror("Unable to open directory.");
        return 1;
    }

    while ((entry = readdir(dir)) != NULL)
    {
        if (entry->d_type == DT_REG)
        {
            file_count++;
        }
    }

    closedir(dir);
    return file_count;
}

void droneProcess(int droneId)
{
    char filePath[256];
    snprintf(filePath, sizeof(filePath), "drone_paths/drone_%d.txt", droneId);

    FILE *file = fopen(filePath, "r");
    if (!file)
    {
        logMessage("Drone %d failed to open path file.\n", droneId);
        exit(EXIT_FAILURE);
    }

    char line[BUFFER_SIZE];
    fgets(line, sizeof(line), file);

    // open semaphore
    char sem_name[64];
    snprintf(sem_name, sizeof(sem_name), "/sem_step_start_%d", droneId);
    sem_t *my_step_start = sem_open(sem_name, 0);
    snprintf(sem_name, sizeof(sem_name), "/sem_step_end_%d", droneId);
    sem_t *my_step_end = sem_open(sem_name, 0);

    while (collision->simulation_running)
    {
        sem_wait(my_step_start);

        if (fgets(line, sizeof(line), file))
        {
            Position pos;
            sscanf(line, "%hd;%hd;%hd", &pos.x, &pos.y, &pos.z);
            pthread_mutex_lock(&mutex_collision);
            collision->pos[droneId] = pos;
            pthread_mutex_unlock(&mutex_collision);
        }
        else
        {
            sem_wait(sem_wait_exec);
            pthread_mutex_lock(&mutex_collision);
            collision->active_drones--;
            pthread_mutex_unlock(&mutex_collision);
            sem_post(sem_wait_exec);
            break;
        }

        pthread_mutex_lock(&mutex_collision);
        if (!collision->simulation_running)
            break;
        pthread_mutex_unlock(&mutex_collision);

        sem_post(my_step_end);
    }
    pthread_mutex_unlock(&mutex_collision);
    sem_post(my_step_end);
    fclose(file);
    exit(EXIT_SUCCESS);
}

int createDroneProcesses()
{
    pid_t pid;

    for (int i = 0; i < drones; i++)
    {
        pid = fork();

        if (pid < 0)
        {
            perror("Fork failed.");
            return 1;
        }
        else if (pid == 0)
        {
            droneProcess(i);
        }
        else
        {
            droneArray[i].id = i;
            droneArray[i].pid = pid;
            droneArray[i].status = 1;
        }
    }

    return 0;
}

int main()
{
    if (loadSettings() != 0)
    {
        logMessage("Error loading settings.\n");
        return 1;
    }
    remove("simulation_output.txt");

    drones = checkNumberOfDrones();
    if (drones < 1)
    {
        logMessage("Error: Unable to determine the number of drones.\n");
        return 1;
    }

    logMessage("===================================\n");
    logMessage("Number of drones: %d\n", drones);

    droneArray = malloc(drones * sizeof(Drone));
    if (droneArray == NULL)
    {
        logMessage("Memory allocation failed.\n");
        return 1;
    }

    shm_fd = shm_open("/collision", O_CREAT | O_RDWR, 0644);
    if (shm_fd < 0)
    {
        logMessage("Failed to open shared memory.\n");
        return 1;
    }

    if (ftruncate(shm_fd, sizeof(CollisionEvent)) < 0)
    {
        logMessage("Failed to truncate shared memory.\n");
        return 1;
    }

    collision = mmap(NULL, sizeof(CollisionEvent), PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    if (collision == MAP_FAILED)
    {
        logMessage("Failed to map shared memory.\n");
        return 1;
    }

    collision->pos = mmap(NULL, drones * sizeof(Position), PROT_READ | PROT_WRITE,
                          MAP_SHARED | MAP_ANONYMOUS, -1, 0);
    collision->drone_ids = mmap(NULL, drones * sizeof(short), PROT_READ | PROT_WRITE,
                                MAP_SHARED | MAP_ANONYMOUS, -1, 0);

    if (collision->pos == MAP_FAILED || collision->drone_ids == MAP_FAILED)
    {
        logMessage("Failed to allocate shared arrays.\n");
        return 1;
    }

    for (int i = 0; i < drones; i++)
    {
        collision->pos[i].x = -1;
        collision->pos[i].y = -1;
        collision->pos[i].z = -1;
        collision->drone_ids[i] = 0;
    }

    collision->collision_detected = 0;
    collision->timeStep = 0;
    collision->simulation_running = 1;
    collision->active_drones = drones;
    collision->totalCollisions = 0;

    // arrays for semaphores
    sem_step_start_array = malloc(drones * sizeof(sem_t *));
    sem_step_end_array = malloc(drones * sizeof(sem_t *));
    if (!sem_step_start_array || !sem_step_end_array)
    {
        logMessage("Failed to allocate semaphore arrays.\n");
        return 1;
    }

    // unlink and create per-drone semaphores
    char sem_name[64];
    for (int i = 0; i < drones; i++)
    {
        snprintf(sem_name, sizeof(sem_name), "/sem_step_start_%d", i);
        sem_unlink(sem_name);
        sem_step_start_array[i] = sem_open(sem_name, O_CREAT, 0644, 0);

        snprintf(sem_name, sizeof(sem_name), "/sem_step_end_%d", i);
        sem_unlink(sem_name);
        sem_step_end_array[i] = sem_open(sem_name, O_CREAT, 0644, 0);

        if (sem_step_start_array[i] == SEM_FAILED || sem_step_end_array[i] == SEM_FAILED)
        {
            logMessage("Failed to open per-drone semaphores.\n");
            return 1;
        }
    }

    sem_unlink("/sem_wait_exec");
    sem_unlink("/sem_collision_start");
    sem_unlink("/sem_collision_end");
    sem_unlink("/sem_report_writer");

    sem_wait_exec = sem_open("/sem_wait_exec", O_CREAT, 0644, 1);
    sem_collision_start = sem_open("/sem_collision_start", O_CREAT, 0644, 0);
    sem_collision_end = sem_open("/sem_collision_end", O_CREAT, 0644, 0);
    sem_report_writer = sem_open("/sem_report_writer", O_CREAT, 0644, 0);

    if (sem_wait_exec == SEM_FAILED || sem_collision_start == SEM_FAILED || sem_collision_end == SEM_FAILED || sem_report_writer == SEM_FAILED)
    {
        logMessage("Failed to open global semaphores.\n");
        return 1;
    }

    pthread_create(&threadCollisionDetector, NULL, collisionDetector, NULL);
    pthread_create(&threadReportWriter, NULL, writeReportThread, NULL);

    if (createDroneProcesses() != 0)
    {
        logMessage("Drone process creation failed.\n");
        return 1;
    }

    while (collision->simulation_running)
    {
        pthread_mutex_lock(&mutex_collision);
        int active = collision->active_drones;
        pthread_mutex_unlock(&mutex_collision);

        if (active <= 0)
            break;

        // trigger all drones
        for (int i = 0; i < drones; i++)
        {
            sem_post(sem_step_start_array[i]);
        }
        // wait for drones
        for (int i = 0; i < drones; i++)
        {
            sem_wait(sem_step_end_array[i]);
        }
        sem_post(sem_collision_start);
        sem_wait(sem_collision_end);

        pthread_mutex_lock(&mutex_collision);
        if (collision->active_drones == 0 || collision->totalCollisions >= maximumCollisionsAllowed)
        {
            collision->simulation_running = 0;
            pthread_mutex_unlock(&mutex_collision);
            break;
        }
        simulationTime++;
        if (!collision->simulation_running)
        {
            pthread_mutex_unlock(&mutex_collision);
            break;
        }

        pthread_mutex_unlock(&mutex_collision);
    }
    // clean up
    pthread_mutex_lock(&mutex_collision);
    collision->simulation_running = 0;
    pthread_cond_broadcast(&cond_collision);
    pthread_mutex_unlock(&mutex_collision);

    for (int i = 0; i < drones; i++)
    {
        sem_post(sem_step_start_array[i]);
        sem_post(sem_step_end_array[i]);
    }
    sem_post(sem_collision_start);

    for (int i = 0; i < drones; i++)
    {
        int status;
        waitpid(droneArray[i].pid, &status, 0);
    }

    pthread_join(threadCollisionDetector, NULL);
    pthread_join(threadReportWriter, NULL);

    for (int i = 0; i < drones; i++)
    {
        sem_close(sem_step_start_array[i]);
        sem_close(sem_step_end_array[i]);
        snprintf(sem_name, sizeof(sem_name), "/sem_step_start_%d", i);
        sem_unlink(sem_name);
        snprintf(sem_name, sizeof(sem_name), "/sem_step_end_%d", i);
        sem_unlink(sem_name);
    }
    free(sem_step_start_array);
    free(sem_step_end_array);

    sem_close(sem_wait_exec);
    sem_close(sem_collision_start);
    sem_close(sem_collision_end);
    sem_close(sem_report_writer);

    sem_unlink("/sem_wait_exec");
    sem_unlink("/sem_collision_start");
    sem_unlink("/sem_collision_end");
    sem_unlink("/sem_report_writer");

    free(droneArray);
    munmap(collision->pos, drones * sizeof(Position));
    munmap(collision->drone_ids, drones * sizeof(short));
    munmap(collision, sizeof(CollisionEvent));
    shm_unlink("/collision");

    return 0;
}