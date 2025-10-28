SOURCES = main.c
EXE = program
CFLAGS = -Wall
LDFLAGS =
LIBS = -pthread -lrt
LD = gcc

OBJECTS = $(SOURCES:%.c=%.o)

default: all

all: $(EXE)

$(EXE): $(OBJECTS)
	$(LD) $(LDFLAGS) $(OBJECTS) -o $(EXE) $(LIBS)

%.o: %.c
	$(LD) $(CFLAGS) -c $< -o $@

clean:
	-rm -f $(EXE)
	-rm -f $(OBJECTS)

run: all
	./$(EXE)