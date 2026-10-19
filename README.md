# Mini Travel Operations System

A simplified travel-operations backend application built with **Java 21** and **Spring Boot 3**. It models the complete operational lifecycle of a travel agency: managing clients, travelers, suppliers, travel requests, quotations, trips, and trip costs, while enforcing strict business rules and data consistency.

This project is a training assignment for **ARYDAR Technologies**, demonstrating production-grade backend architecture, database modeling, REST API design, and automated testing.

---

## 🚀 Key Features & Business Workflows

- **Client & Traveler Management:** Full CRUD for clients and their associated travelers. Deletion guardrails prevent deleting clients with active requests or trips.
- **Supplier Management:** Vendor management across categories (`HOTEL`, `TRANSPORT`, `GUIDE`, `ACTIVITY`, etc.), with safeguards against deleting suppliers referenced by trip costs.
- **Travel Request Management:** Track client destinations, travel dates, traveler counts, and budgets, with date-order and positive-number validations.
- **Quotation Lifecycle:** Manage multiple quotes per request. Enforces valid, non-expired quotes and guarantees that only one quotation can be accepted per request.
- **Trip Conversion & Execution:** Convert accepted quotations directly into trips. Tracks operational status (`PLANNED`, `CONFIRMED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).
- **Trip Cost Management:** Record supplier line-item costs per trip. Paid costs are immutable (cannot be edited or deleted), and a trip cannot be marked `COMPLETED` while unpaid costs remain.
- **Unified Error Handling:** A global `@RestControllerAdvice` returns consistent, standard JSON error responses across all endpoints.
- **Web Dashboard & Swagger UI:** Interactive OpenAPI documentation and an embedded single-page frontend for quick operational testing.

---

## 🛠️ Tech Stack

| Area | Technology |
|------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.4 |
| Data Access | Spring Data JPA & Hibernate |
| Database | PostgreSQL |
| Schema Versioning | Flyway |
| Security | Spring Security (HTTP Basic Auth, role-based access) |
| API Docs | OpenAPI / Swagger UI (springdoc-openapi) |
| Testing | JUnit 5 & Mockito |
| Build Tool | Maven 3.8+ |

---

## 🧱 Layered Architecture

```text
src/main/java/com/arydar/travel/
├── config/        # Spring Security & application configuration
├── controller/    # REST controllers & HTTP mappings
├── dto/           # Request & response data transfer objects
├── entity/        # JPA entities
├── enumtype/      # System enums (statuses & categories)
├── exception/     # Custom exceptions & centralized global handler
├── repository/    # Spring Data JPA repositories
└── service/       # Service interfaces & business logic implementations
```

---

## 🛢️ Entity-Relationship Model

```text
CLIENT (1) ───────── (N) TRAVELER
   │
   ├────── (N) ───► TRAVEL_REQUEST (1) ───────── (N) QUOTATION
   │                                                   │
   │                                                   │ (1 accepted)
   │                                                   ▼
   └────── (N) ───► TRIP (1) ◄─────────────────────────┘
                      │
                      │ (1)
                      ▼
                 TRIP_COST (N) ◄─────── (1) SUPPLIER
```

---

## 🚦 Getting Started

### Prerequisites

- **JDK:** version 21 or higher
- **Database:** PostgreSQL (running locally or via Docker)
- **Build Tool:** Maven 3.8+

### Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE travel_ops_db;
```

Verify your credentials in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/travel_ops_db
    username: postgres
    password: your_postgres_password
```

### Running the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/mini-travel-ops.git
   cd mini-travel-ops
   ```

2. Build and run with Maven:

   ```bash
   mvn clean spring-boot:run
   ```

On startup, Flyway automatically executes `V1__initial_schema.sql` to create the tables and relational foreign keys.

---

## 🔗 Interactive Interfaces & API Documentation

- **Web Application Dashboard:** <http://localhost:8080/>
- **Swagger / OpenAPI Docs:** <http://localhost:8080/swagger-ui/index.html>

### Default Credentials (HTTP Basic Authentication)

| Role | Username | Password | Permissions |
|------|----------|----------|-------------|
| Employee | `kedar` | `password123` | Read/Write access to operational endpoints |
| Admin | `admin` | `admin123` | Full access, including `DELETE` operations |

> ⚠️ These credentials are for local development and training only. Change them before any real deployment.

---

## 🧪 Running Automated Tests

Run the unit tests, which cover business rules and edge cases (e.g., expired quotation rejection, unpaid-cost trip completion blocks, and paid-cost deletion restrictions):

```bash
mvn test
```

---

## 📋 Mandatory Business Rules Enforced

**Dates**
- Trip/request end dates cannot precede start dates.

**Requests & Quotes**
- Quotes require a positive total amount (`> 0`).
- Only one quotation can be marked `ACCEPTED` per request.
- Expired quotes cannot be accepted.

**Trips**
- Trips can only be created from `ACCEPTED` quotations.
- Cancelled trips cannot receive new costs.
- Trips with unpaid or partially paid costs cannot be marked `COMPLETED`.

**Costs & Deletion Guardrails**
- Cost amounts must be positive (`> 0`).
- Costs with status `PAID` are immutable (cannot be edited or deleted).
- Suppliers referenced in trip costs cannot be deleted.
- Clients with active requests or trips cannot be deleted.

---

## 📄 License

This project was created for training purposes at ARYDAR Technologies.
