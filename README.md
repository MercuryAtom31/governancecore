# Governance Core — ISMS/GRC Platform

Governance Core is a production-focused Information Security Management System (ISMS) backend built with Spring Boot. It centralizes security governance in one platform, replacing scattered spreadsheets and documents with structured, audit-ready workflows.

## Product Vision

Organizations pursuing compliance certifications (ISO 27001, SOC 2) struggle to maintain traceability between their assets, risks, controls, and evidence. Governance Core solves this by providing a single system where security teams, engineering managers, and auditors can collaborate.

The core outcome is end-to-end traceability:

```
Asset → Risk → Control → Evidence
```

## Who This Is For

| Role | How they use it |
|---|---|
| Security / GRC Teams | Manage risks, map controls, track compliance status |
| Engineering Managers | Own assets, assign access, track remediation |
| Auditors / Compliance | Review evidence, verify control implementation |

## Current Status

**Phase 1 — AssetSubdomain (Active)**

The first vertical slice of the platform is being built following DDD subdomain organization with n-tier layering. Once complete, this pattern is replicated for every subsequent subdomain.

Current capabilities:
- Create an asset
- List all assets (paginated)
- Get asset by ID
- Update an asset
- Delete an asset

## Architecture

The backend is organized as DDD subdomains, each with strict n-tier layering inside:

```
assetsubdomain/
  presentationlayer/    → REST controllers + request/response DTOs
  businesslayer/        → service interfaces + use-case implementations
  datalayer/            → JPA entities + repositories
  datamapperlayer/      → MapStruct mappers (entity ↔ DTO)

shared/
  exceptions/           → custom exception classes
  advice/               → @RestControllerAdvice (global error handling)
```

**Subdomain boundary rule:** subdomains reference each other only by ID — no entity imports across subdomain boundaries. This keeps the architecture clean and ready to scale.

## Tech Stack

### Current
| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot 3.5.x | Application framework |
| Spring Web | REST API layer |
| Spring Data JPA | Database access |
| PostgreSQL | Production database |
| Flyway | Database schema migrations |
| Jakarta Validation | Request validation (`@NotBlank`, etc.) |
| Lombok | Boilerplate reduction |
| MapStruct | Type-safe DTO ↔ entity mapping |
| Gradle (Groovy) | Build tool |

### Testing
| Technology | Purpose |
|---|---|
| JUnit 5 | Test framework |
| Mockito | Service unit tests |
| MockMvc | Controller integration tests |
| H2 | In-memory database for fast test runs |

### Planned
| Technology | Purpose |
|---|---|
| Spring Security | Endpoint protection |
| OAuth2 (Google / GitHub) | Delegated authentication — no password management |
| RBAC | Role-based access (Admin, Analyst, Auditor) |
| Docker + docker-compose | Containerized deployment |

### Frontend
| Technology | Purpose |
|---|---|
| React | Component-based UI |
| TypeScript | Type-safe frontend development |
| CSS | Styling |

## Authentication Design

The platform will use **OAuth2** for authentication (via Google or GitHub), meaning users log in through a trusted identity provider rather than managing passwords directly. This is implemented using Spring Security's built-in OAuth2 support — not a third-party service like Auth0.

After OAuth2 login, the platform issues tokens for session management and enforces **role-based access control (RBAC)**:

- **Admin** — full platform access
- **Analyst** — create and manage risks, controls, assets
- **Auditor** — read-only access to evidence and reports

## Database Migrations

Flyway manages all schema changes in a versioned, ordered manner — think of it as Git for your database schema.

Migration scripts live in:
```
src/main/resources/db/migration/
```

Naming convention:
```
V1__create_assets_table.sql
V2__create_risks_table.sql
V3__add_description_to_assets.sql
```

Flyway runs automatically at startup and only applies migrations that have not yet been executed.

## Local Development Setup

### Prerequisites
- Java 21
- PostgreSQL running locally
- Gradle

### Database configuration

In `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/governancecore
    username: your_user
    password: your_password
  flyway:
    enabled: true
    locations: classpath:db/migration
```

### Run the application

```bash
# Linux / Mac
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

Flyway migrations execute automatically at startup before any JPA operations.

### Run tests

```bash
./gradlew test
```

Integration tests use H2 in-memory database — no PostgreSQL required for testing.

## Roadmap

| Phase | Subdomain | Description |
|---|---|---|
| 1 ✅ | AssetSubdomain | Asset inventory — the foundation everything attaches to |
| 2 | RiskSubdomain | Risk register with automatic scoring (likelihood × impact) |
| 3 | ControlSubdomain | ISO 27001 control catalog + Statement of Applicability |
| 4 | EvidenceSubdomain | Audit evidence metadata + links to risks/controls/assets |
| 5 | Security | OAuth2 login + Spring Security + RBAC |
| 6 | IAMSubdomain | Access assignments, access reviews, quarterly workflow |
| 7 | AppSecSubdomain | Vulnerability tracking + SDLC artifacts + compliance mapping |
| 8 | Reporting | Audit-ready traceability reports (Asset → Risk → Control → Evidence) |
| 9 | Deployment | Docker + docker-compose + React/TypeScript frontend |

## Project Status

Active development — Phase 1 in progress.