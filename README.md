# ESG Reporting Backend

## Project Overview

The ESG Reporting Backend is a RESTful API application developed using Java and Spring Boot to support Environmental, Social, and Governance (ESG) reporting. It provides APIs to manage ESG reports, maintain sustainability metrics, track report revisions, manage supporting evidence, and generate reports in PDF and Word formats.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Spring Security
- Maven
- REST APIs
- Swagger / OpenAPI
- Apache POI
- OpenPDF

## Key Features

- Create and manage ESG reports.
- Record Environmental, Social, Governance, and Ethics metrics.
- Retrieve dashboard data for ESG reports.
- Manage report submission, approval, and change-request workflows.
- Maintain report revision history.
- Upload and manage supporting evidence documents.
- Generate reports in PDF and Word formats.
- Provide REST API documentation through Swagger/OpenAPI.
- Connect to a MySQL database for persistent data storage.

## Project Structure

```text
esg--reporting-backend/
├── src/
│   ├── main/
│   │   ├── java/com/esg/reporting/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── exception/
│   │   │   ├── security/
│   │   │   ├── EsgReportingBackendApplication.java
│   │   │   ├── HealthController.java
│   │   │   └── SecurityConfig.java
│   │   └── resources/
│   └── test/
├── .gitignore
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Prerequisites

- Java Development Kit (JDK) 21
- MySQL Server
- Git
- An IDE such as Eclipse
- Postman or a web browser for API testing

## Setup and Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Aishwarya-a3/esg--reporting-backend.git
   ```

2. Open the project in Eclipse as an existing Maven project.

3. Create a MySQL database named `esg_reporting`.

4. Configure the database connection in your local `application.properties` file. Set the appropriate MySQL username and password for your environment.

5. Ensure the database configuration and security credentials remain private and are not committed to GitHub.

6. Run the main application class:

   `EsgReportingBackendApplication.java`

7. The application will start on the default port `8080`, unless configured otherwise.

## API Documentation

When the backend is running locally, access the Swagger UI at:

http://localhost:8080/swagger-ui/index.html

OpenAPI specification:

http://localhost:8080/v3/api-docs

## Main API Modules

- **ESG Reports:** Create, retrieve, and update reports.
- **Environmental Metrics:** Manage environmental reporting data.
- **Social Metrics:** Manage social responsibility indicators.
- **Governance Metrics:** Manage governance-related indicators.
- **Ethics Metrics:** Record ethics and compliance data.
- **Dashboard:** Retrieve ESG reporting summaries.
- **Evidence Management:** Upload and manage supporting documents.
- **Report Export:** Generate PDF and Word reports.
- **Report Workflow:** Submit reports, approve them, or request changes.

## Database

The application uses MySQL for persistent storage and Spring Data JPA for database operations. Entity classes represent ESG reports, metric records, evidence, and revision history.

## Security

The application includes Spring Security configuration for protecting API endpoints. Configure credentials and security settings appropriately before running the application in any shared or production environment.

**Important:** Do not publish database passwords, private documents, or production credentials in this repository.

## Testing

The project includes a Spring Boot application test under `src/test/java`. Additional API tests can be performed using Postman and Swagger UI.

## Limitations and Future Enhancements

- Add comprehensive unit and integration tests.
- Strengthen role-based access control and organization-level data isolation.
- Improve audit logging and revision integrity.
- Expand report exports to include detailed metric data.
- Add production-ready deployment configuration and externalized secrets management.
- Integrate verified ESG data sources and automated validation.

## Disclaimer

This project is developed for learning and demonstration purposes. Any sample ESG data used for testing should not be interpreted as verified organizational or sustainability data.

## Author

**Aishwarya Bhandari**

Java Full Stack Developer | Information Science and Engineering
