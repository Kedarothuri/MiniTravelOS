Markdown# Mini Travel Operations System

A simplified travel-operations backend application built using **Java 21** and **Spring Boot 3**. This project models the complete operational lifecycle of a travel agency—managing clients, travelers, suppliers, travel requests, quotations, trips, and trip costs while enforcing strict business rules and data consistency.

This project serves as a training assignment for ARYDAR Technologies to demonstrate production-grade backend architecture, database modeling, REST API design, and automated testing.

---

## 🚀 Key Features & Business Workflows

- **Client & Traveler Management:** Full CRUD operations for clients and their associated travelers. Includes deletion guardrails to prevent deleting clients with active requests/trips.
- **Supplier Management:** Vendor management across categories (`HOTEL`, `TRANSPORT`, `GUIDE`, `ACTIVITY`, etc.) with safeguards against deleting suppliers referenced by trip costs.
- **Travel Request Management:** Track client destinations, travel dates, traveler counts, and budgets with date order and positive numeric validations.
- **Quotation Lifecycle:** Manage multiple quotes per request. Enforces valid non-expired quotes and guarantees that **only one quotation can be accepted per request**.
- **Trip Conversion & Execution:** Convert accepted quotations directly into active trips. Tracks operational trip status (`PLANNED`, `CONFIRMED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).
- **Trip Cost Management:** Record supplier line-item costs per trip. Enforces immutability on paid costs (paid costs cannot be edited or deleted) and prevents marking a trip as `COMPLETED` while unpaid costs remain.
- **Unified Error Handling:** Global `@RestControllerAdvice` providing consistent standard JSON error responses across all endpoints.
- **Web Dashboard & Swagger UI:** Interactive OpenAPI documentation and an embedded single-page frontend UI for quick operational testing.

---

## 🛠️ Tech Stack & Architecture

- **Language:** Java 21
- **Framework:** Spring Boot 3.3.4
- **Data Access:** Spring Data JPA & Hibernate
- **Database:** PostgreSQL
- **Schema Versioning:** Flyway Migration
- **Security:** Spring Security (HTTP Basic Auth & Role-based Access)
- **Documentation:** OpenAPI / Swagger UI (springdoc-openapi)
- **Testing:** JUnit 5 & Mockito

### Layered Architecture
```text
src/main/java/com/arydar/travel/
├── config/             # Spring Security & App Configurations
├── controller/         # REST Controllers & HTTP Mappings
├── dto/                # Request & Response Data Transfer Objects
├── entity/             # JPA Entities
├── enumtype/           # System Enums (Statuses & Categories)
├── exception/          # Custom Exceptions & Centralized Global Handler
├── repository/         # Spring Data JPA Repositories
└── service/            # Service Interfaces & Business Logic Implementation
🛢️ Entity-Relationship ModelPlaintext CLIENT (1) <─────── (N) TRAVELER
   │
   ├─────── (N) ───► TRAVEL_REQUEST (1) <─────── (N) QUOTATION
   │                                                       │ (1)
   └─────── (N) ───► TRIP (1) <────────────────────────────┘ (Accepted)
                       │ (1)
                       ▼
                   TRIP_COST (N) ◄─────── (1) SUPPLIER
🚦 Getting StartedPrerequisitesJava Development Kit (JDK): Version 21 or higher   Database: PostgreSQL (running locally or via Docker)[cite: 1]Build Tool: Maven 3.8+[cite: 1]Database SetupCreate a PostgreSQL database locally:SQLCREATE DATABASE travel_ops_db;
Verify your PostgreSQL credentials in src/main/resources/application.yml:YAMLspring:
  datasource:
    url: jdbc:postgresql://localhost:5432/travel_ops_db
    username: postgres
    password: your_postgres_password
Running the ApplicationClone the repository:Bashgit clone [https://github.com/your-username/mini-travel-ops.git](https://github.com/your-username/mini-travel-ops.git)
cd mini-travel-ops
Build and run using Maven:Bashmvn clean spring-boot:run
Flyway will automatically execute V1__initial_schema.sql on startup to set up tables and relational foreign keys[cite: 1].🔗 Interactive Interfaces & API DocumentationWeb Application Dashboard: Open http://localhost:8080/ in your browser[cite: 1].Swagger / OpenAPI Documentation: Open http://localhost:8080/swagger-ui/index.html[cite: 1].Default Credentials (HTTP Basic Authentication)RoleUsernamePasswordPermissionsEmployeekedarpassword123Read/Write access to operational endpoints[cite: 1]Adminadminadmin123Full access, including DELETE operations[cite: 1]🧪 Running Automated TestsTo execute unit tests covering business rules and edge cases (e.g., verifying expired quotation rejections, unpaid trip completion blocks, and paid cost deletion restrictions)[cite: 1]:Bashmvn test
📋 Mandatory Business Rules EnforcedDates: Trip/Request end dates cannot precede start dates[cite: 1].Requests & Quotes: Quotes require positive total amounts ($> 0$). Only one quotation can be marked ACCEPTED per request[cite: 1]. Expired quotes cannot be accepted[cite: 1].Trips: Trips can strictly be created from ACCEPTED quotations[cite: 1]. Cancelled trips cannot receive new costs[cite: 1]. Trips with unpaid or partially paid costs cannot be marked COMPLETED[cite: 1].Costs & Deletion Guardrails: Cost amounts must be positive ($> 0$)[cite: 1]. Costs with a status of PAID are immutable (cannot be edited or deleted)[cite: 1]. Suppliers referenced in trip costs cannot be deleted[cite: 1]. Clients with active requests/trips cannot be deleted[cite: 1].
<FollowUp>
Would you like help with the Git commands to initialize, commit, and push this repository to GitHub?
</FollowUp>
