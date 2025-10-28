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

#define BUFFER_SIZE 80

typedef struct {
    short x;
    short y;
    short z;
} Position;

typedef struct {
    short id;
    size_t pid;
    char status;
} Drone;

const char FILE_DELIMITER = ';';

Drone ** currentSimulationArray = NULL;
Drone ** previousSimulationArray = NULL;
Drone * droneArray = NULL;
int drones = 0;
int fd[2];
char * droneMessage = NULL;
char droneMessageStatus = 0;

Drone threadDrone;

int totalCollisions = 0;
int maximumCollisionsAllowed = 5;

int simulationTime = 0;

int ARRAY_SIZE = 100;

int loadSettings() {
    FILE *file = fopen("settings.txt", "r");
    if (file == NULL) {
        perror("Unable to open settings file.");
        return 1;
    }
    char line[256];
    while (fgets(line, sizeof(line), file)) {
        if (sscanf(line, "maximumCollisionsAllowed=%d", &maximumCollisionsAllowed) == 1) {
            break;
        }
        if (sscanf(line, "arraySize=%d", &ARRAY_SIZE) == 1) {
            break;
        }
    }
    fclose(file);
    return 0;
}

void logMessage(const char *message, ...) {
    //this allows things like %d and %s
    va_list args1, args2;

    char messageWithPID[BUFFER_SIZE];
    snprintf(messageWithPID, sizeof(messageWithPID), "[%d] %s", getpid(), message);

    va_start(args1, message);
    va_copy(args2, args1);

    vfprintf(stderr, messageWithPID, args1);

    //open file for writing
    int fdw = open("simulation_output.txt", O_WRONLY | O_CREAT | O_APPEND, 0644);
    if (fdw >= 0) {
        //write to file
        vdprintf(fdw, messageWithPID, args2); 
        close(fdw);
    }

    va_end(args1);
    va_end(args2);
}

int getIndex(int x, int y, int z) {
    return x * ARRAY_SIZE * ARRAY_SIZE + y * ARRAY_SIZE + z;
}

Drone * getDroneByPID(size_t pid){
    for(int i = 0; i<drones; i++){
        if(droneArray[i].pid==pid){
            return &droneArray[i];
        }
    }
    return NULL;
}

int checkNumberOfDrones() {
    struct dirent *entry;
    int file_count = 0;
    DIR *dir = opendir("drone_paths/");
    if (dir == NULL) {
        perror("Unable to open directory.");
        return 1;
    }

    while ((entry = readdir(dir)) != NULL) {
        if (entry->d_type == DT_REG) {
            file_count++;
        }
    }

    closedir(dir);

    return file_count;
}

int getDroneIndex(Drone d){
    for(int i = 0; i<drones; i++){
        if(droneArray[i].id==d.id){
            return i;
        }
    }
    return -1;
}

void requestToSetDronePosition(int signo) {
    close(fd[0]);
    int n = write(fd[1], droneMessage, strlen(droneMessage)+1);
    if (n == -1) {
        perror("Error writing to pipe.");
    }
}

void collisionDetected(int signo){
    logMessage("Drone %d collided.\n", threadDrone.id);
    //_exit(1);
}

void setThreadDrone(int droneId, pid_t pid) {
    sigset_t mask;
    sigemptyset(&mask);
    sigaddset(&mask, SIGUSR1);

    char filename[256];
    snprintf(filename, sizeof(filename), "drone_paths/drone_%d.txt", droneId);
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        perror("Unable to open file.");
        exit(1);
    }
    char line[256];
    fgets(line, sizeof(line), file);
    while (fgets(line, sizeof(line), file)) {
        droneMessage = line;
        sigsuspend(&mask);
    }
    close(fd[1]);
    fclose(file);
    exit(1);
}

int changeDronePosition(Drone * d, Position p) {
    int index = getIndex(p.x, p.y, p.z);
    if(currentSimulationArray[index] != NULL){
        totalCollisions++;
        return currentSimulationArray[index]->pid;
    }
    currentSimulationArray[index] = d;
    return 0;
}

int main() {
    //load settings
    if (loadSettings() != 0) {
        logMessage("Error loading settings.\n");
        return 1;
    }
    //clear previous logs
    remove("simulation_output.txt");

    //pipe for drone communication
    if (pipe(fd) == -1) {
        perror("Pipe failed.");
        return 1;
    }

    //check number of drones
    drones = checkNumberOfDrones();
    if (drones < 1) {
        logMessage("Error: Unable to determine the number of drones.\n");
        return 1;
    }
    logMessage("===================================\n");
    logMessage("Number of drones: %d\n", drones);

    //allocate memory for drones
    droneArray = malloc(drones * sizeof(Drone));
    if (droneArray == NULL) {
        logMessage("Memory allocation failed.");
        return 1;
    }

    for(int i = 0; i < drones; i++) {
        droneArray[i].status = 1;
    }

    pid_t pid;

    //create drone processes
    for (int i = 0; i < drones; i++) {
        pid = fork();

        if (pid < 0) {
            perror("Fork failed.");
            return 1;
        } else if (pid == 0) {
            threadDrone.id = i;
            threadDrone.pid = getpid();
            threadDrone.status = 1;
            break;
        } else {
            droneArray[i].id = i;
            droneArray[i].pid = pid;
            droneArray[i].status = 1;
        }
    }

    //drone
    if(pid == 0) {
        //ISR for SIGUSR1
        struct sigaction act;
        memset(&act, 0, sizeof(struct sigaction));
        act.sa_handler = collisionDetected;
        act.sa_flags = SA_RESTART;
        sigemptyset(&act.sa_mask);
        sigaddset(&act.sa_mask, SIGUSR2);
        sigaction(SIGUSR1, &act, NULL);

        //ISR for SIGUSR2
        struct sigaction act2;
        memset(&act2, 0, sizeof(struct sigaction));
        act2.sa_handler = requestToSetDronePosition;
        act2.sa_flags = SA_RESTART;
        sigaction(SIGUSR2, &act2, NULL);

        //drone process
        setThreadDrone(threadDrone.id, getpid());
        exit(0);
    }

    //maestro
    if(pid > 0) {
        size_t totalElements = ARRAY_SIZE * ARRAY_SIZE * ARRAY_SIZE;
        currentSimulationArray = malloc(totalElements * sizeof(Drone*));
        if (currentSimulationArray == NULL) {
            logMessage("Memory allocation failed.");
            return 1;
        }
        previousSimulationArray = malloc(totalElements * sizeof(Drone*));
        if (previousSimulationArray == NULL) {
            logMessage("Memory allocation failed.");
            return 1;
        }

        //cleanup for simulation start
        for(int i = 0; i<totalElements; i++){
            currentSimulationArray[i] = NULL;
            previousSimulationArray[i] = NULL;
        }

        logMessage("Starting simulation.\n");
        logMessage("===================================\n");

        int dronesRunning = drones;
        while (dronesRunning){
            for(int i = 0; i < drones; i++) {
                if(droneArray[i].status==1){
                    kill(droneArray[i].pid, SIGUSR2);
                    //usleep(10000);
                    Position p = {-1, -1, -1};
                    close(fd[1]);
                    char * message = malloc(BUFFER_SIZE);
                    if (message == NULL) {
                        logMessage("Memory allocation failed.");
                        return 1;
                    }
                    int n = read(fd[0], message, BUFFER_SIZE);
                    if (n == -1) {
                        perror("Error reading from pipe.");
                        free(message);
                        return 1;
                    }
                    message[n] = '\0';
                    char *save_ptr;
                    char *token = strtok_r(message, &FILE_DELIMITER, &save_ptr);
                    if (token != NULL) {
                        p.x = atoi(token);
                        token = strtok_r(NULL, &FILE_DELIMITER, &save_ptr);
                    }
                    if (token != NULL) {
                        p.y = atoi(token);
                        token = strtok_r(NULL, &FILE_DELIMITER, &save_ptr);
                    }
                    if (token != NULL) {
                        p.z = atoi(token);
                    }
                    
                    if(p.x == -1 || p.y == -1 || p.z == -1){
                        dronesRunning--;
                        droneArray[i].status=-1;
                        logMessage("Drone %d finished its routine. [TIME=%d]\n", droneArray[i].id, simulationTime);
                    }
                    else{
                        logMessage("Moving drone %d to (%d,%d,%d). [TIME=%d]\n", i, p.x, p.y, p.z, simulationTime);
                        int move = changeDronePosition(&droneArray[i], p);
                        if(move != 0){
                            kill(droneArray[i].pid, SIGUSR1);
                            kill(move, SIGUSR1);
                            if(totalCollisions > maximumCollisionsAllowed){
                                logMessage("Maximum collisions exceeded. Terminating simulation.\n");
                                for(int i = 0; i < drones; i++) {
                                    kill(droneArray[i].pid, SIGINT);
                                    wait(NULL);
                                }
                                exit(1);
                            }
                        }
                    }
                }
            }

            //cleanup for next simulation step (passing current array to previous)
            for(int i = 0; i<totalElements; i++){
                previousSimulationArray[i] = currentSimulationArray[i];
                currentSimulationArray[i] = NULL;
            }

            simulationTime++;
        }
        close(fd[0]);
        logMessage("=======================================\n");
        logMessage("Collision count: %d\n", totalCollisions);
        logMessage("Maximum collisions allowed: %d\n", maximumCollisionsAllowed);
        logMessage("Total simulation time: %d\n", simulationTime);
        logMessage("Simulation completed successfully.\n");
        logMessage("=======================================\n");
    }

    //cleanup
    free(previousSimulationArray);
    free(currentSimulationArray);
    free(droneArray);
    return 0;
}
