# Thermal Point WebSocket Service

This is a Spring Boot application that provides real-time WebSocket services for thermal points in forestry areas. The service connects to a PostgreSQL database to retrieve and manage thermal point data.

## Getting Started

### Prerequisites

- Java 17
- Gradle
- PostgreSQL

### Building the Project

To build the project, use the following command:

./gradlew build

### Running the Project

To run the project, use the following command:

./gradlew bootRun

### Configuration

The project is configured to connect to a PostgreSQL database. Update the application.yml file with your database connection details.

### Database

The project uses PostgreSQL for storing forestry and thermal point data. Ensure that your database is set up and the connection details are correctly configured in application.yml.

### Dependencies

The project uses the following dependencies:

- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Spring Boot Starter WebSocket
- Spring Boot Starter AMQP
- PostgreSQL JDBC Driver


### License

This project is licensed under the MIT License - see the LICENSE file for details.