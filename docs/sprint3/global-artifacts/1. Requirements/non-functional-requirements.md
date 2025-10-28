# Non-Functional Requirements

## NFR01 - Project management using scrum
Scrum should be used for project management. The group’s LAPR4 PL teacher will be the Scrum Master
and there will be one weekly scrum meeting with the scrum Master during the LAPR4 PL+OT class.
The Development Process document, available at Moodle7, provides detailed information regarding this requirement.

## NFR02 - Technical Documentation
Project documentation should be always available on the project repository ("docs" folder, markdown
format) and, when applicable, in accordance to the UML notation. The development process of every
US (e.g.: analysis, design, testing, etc.) must be reported (as part of the documentation). Whenever
possible, the PlantUML tool shall be used to generate diagrams. The diagrams’ source files and the
actual diagrams in a vector format (PNG) must be included in the repository.

## NFR03 - Test-driven development
The team should aim to adopt a test-driven development approach.

## NFR04 - Source Control
The source code of the solution as well as all the documentation and related artifacts should be
versioned in a GitHub repository to be provided to the students. Only the main (master/main) branch
will be used (e.g., as a source for releases)

## NFR05 - Continuous Integration
The Github repository will provide night builds with publishing of results and metrics.

## NFR06 - Deployment and Scripts
The repository should include the necessary scripts to build and deploy the solution in a variety of
systems (at least Linux and Windows). It should also include a readme.md file in the root folder
explaining how to build, deploy and execute the solution.

## NFR07 - Database by configuration
The system must support that data persistence is done either "in memory" or in a relational database
(RDBMS). Although in-memory database solutions can be used during development and testing, the
solution must include a final deployment where a remote persistent relational database is used. The
system should have the ability to initialize some default data.

## NFR08 - Authentication and Authorization
The system must support and apply authentication and authorization for all its users and
functionalities.

## NFR09 - Programming language
The solution should be implemented using Java as the main language. Other languages can be used in
accordance with more specific requirements.

## NFR10 – Network sockets APIs
The network sockets APIs may either implement a new application protocol developed for this purpose
or use an existing standard application protocol, like HTTP.

## NFR11 – High-level language (DSL) and drones’ language analysis/validation
The support for this functionality must follow specific technical requirements provided in LPROG. The
[ANTLR](https://www.antlr.org/) tool should be used.

## NFR12 – Simulation system in sprint 3
Shodrone wants the simulation system to use a multithreaded parent process and child drone
processes communicating via shared memory, with functionalities separated into dedicated threads
(collision detection and report generation), so that the simulation runs step-by-step with robust
synchronization using semaphores and can be terminated via signals when a collision threshold is
exceeded.