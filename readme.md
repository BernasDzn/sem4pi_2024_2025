# Project Shodrone

This repository contains the Shodrone project — a multi-module Java application focused on modelling and processing drone show proposals and simulations. The project includes core domain logic, a plugin for processing show proposal templates, a show simulator, and bootstrap data to seed the application for development/testing.

## 1. Description of the Project

Shodrone aims to provide tooling to:
- Define and validate show proposal templates (text-based templates with placeholders).
- Generate final proposal documents by substituting placeholders with runtime values.
- Model domain entities related to drone shows.
- Simulate shows for validation/testing purposes.

Primary modules (high level):
- shodrone.core — domain model, application services and repositories (show proposals, templates, etc.).
- shodrone.sp_plugin — a plugin that validates and processes show proposal templates (contains SPLexer/SPParser and DocumentProcessor).
- shodrone.showsimulator — simulator components and artifacts.
- shodrone.bootstrap — bootstrappers to seed sample data.

See the Documentation section for requirements, design artifacts, and sprint planning.

## 2. Planning and Technical Documentation

The documentation for the current sprint and other artifacts lives in the docs/ folder:

- Documentation root: docs/README.md
- Sprint documentation: docs/sprint3/readme.md
- Specific user stories and artifacts under docs/sprint3 and docs/_UNORGANIZED

Open: docs/sprint3/readme.md for planning, user stories and design artifacts.

## 3. How to Build

Prerequisites
- JDK 17 (or JDK 11+ — ensure compatibility with your environment)
- Apache Maven 3.6+
- Git

From the repository root, run:

- To build and run tests:
  mvn clean install

- To build quickly (skip tests — not recommended for CI):
  mvn -DskipTests clean package

You can run Maven in parallel to speed up builds:
  mvn -T 1C clean install

The project is multi-module; building at the repo root will build all modules.

## 4. How to Execute Tests

Run all unit tests and integration tests (if configured) with:

  mvn test

To run tests for a single module, use:
  mvn -pl shodrone.core test
or
  mvn -pl shodrone.sp_plugin test

Note: Some tests may expect resources from src/test/resources (e.g., template files). Ensure your working directory is the repository root when running tests.

## 5. How to Run

There are no single-click launcher jars in the repository root. Example ways to run parts of the project:

- Run DocumentProcessor (template processing) for local testing
  The sp_plugin module contains a test main you can run from your IDE or via Maven exec:

  mvn -pl shodrone.sp_plugin exec:java -Dexec.mainClass="shodrone.sp_plugin.DocumentProcessor"

  This will launch the sample main in DocumentProcessor (interactive prompts may be used by that main). Alternatively run DocumentProcessor from your IDE by running the class shodrone.sp_plugin.DocumentProcessor.

- Run the show simulator:
  If the simulator module contains a main class, run it from your IDE or via Maven exec. Check shodrone.showsimulator/docs/README.md for simulator-specific instructions.

If you prefer packaged jars:
1. Build the project: mvn -DskipTests clean package
2. Locate the produced jar(s) under the target/ directories of each module and run:
   java -jar path/to/module/target/<artifact>-<version>.jar
(Only modules configured to produce executable jars can be run this way.)

## 6. How to Install/Deploy into Another Machine (or Virtual Machine)

1. Ensure target machine has the required JDK and Maven (or at least JRE if running produced jars).
2. Clone the repository:
   git clone https://github.com/BernasDzn/sem4pi_2024_2025.git
3. Build on the target machine:
   mvn -DskipTests clean package
4. Copy the needed module jar(s) to the desired location and run them with:
   java -jar <module-jar>.jar

For Docker or other containerized deployment, create a Dockerfile that installs a JDK, copies the module jar, and runs java -jar. This project does not yet include official Docker artifacts.

## 7. How to Generate PlantUML Diagrams

There is a helper script to generate PlantUML diagrams used in documentation:

  ./generate-plantuml-diagrams.sh

Run the script from the repository root on Unix-like systems (Linux / macOS). The script will generate diagrams into the docs folders as configured.

If you cannot run the script on your platform, you can generate PlantUML diagrams manually using the PlantUML tool or an online PlantUML service, using the .puml files stored in the repository.

## Project Structure (high level)

- shodrone.core/ — core domain, entities, repositories, application controllers (e.g., ConfigureTemplateController)
- shodrone.sp_plugin/ — document validator, parser, DocumentProcessor and plugin-related code
- shodrone.showsimulator/ — simulation inputs, components and docs
- shodrone.bootstrap/ — bootstrappers used to seed demo/sample data
- docs/ — project documentation, sprint artifacts, user stories
- LICENSE — MIT license

## Contributing

Contributions are welcome. Typical workflow:
1. Fork the repository.
2. Create a branch for your feature/fix.
3. Implement your changes and add/update tests.
4. Run the test suite locally: mvn test
5. Submit a pull request with a clear description of what you changed and why.

Please follow existing code style and add/update documentation and tests for any substantial changes.

## Known Notes & Tips

- Template validation and processing is implemented in shodrone.sp_plugin and shodrone.core contains ShowProposalTemplate domain logic and tests.
- Several user stories and design artifacts are in docs/sprint3 and docs/_UNORGANIZED — consult them for domain rules and acceptance criteria.
- Some tests expect resource files under src/test/resources (for example template files like Proposta_SP_01.txt). Keep working directory at the repo root when running tests.

## License

This project is licensed under the MIT License — see the LICENSE file for details.

## Contact

Repository owner / primary author: Bernardo Cardoso (see GitHub profile: https://github.com/BernasDzn)

If you need help understanding a module, raise an issue in the repository with a clear description and relevant logs or stack traces.
