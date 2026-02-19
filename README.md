# Governance Core (ISMS Platform)

Governance Core is a production-focused ISMS/GRC platform designed for organizations that need audit-ready security governance workflows. It centralizes assets, risks, controls, evidence, and traceability in one system.

## Product Vision
Build an audit-ready platform where teams can map:
- Asset -> Risk -> Control -> Evidence

This enables certification/compliance readiness (for example ISO 27001-oriented workflows).

## Current Scope (Phase 1)
The project currently focuses on the **AssetSubdomain** MVP:
- Create asset
- List assets
- Get asset by ID
- Update asset
- Delete asset

## Architecture
The backend follows **DDD subdomain organization** with **n-tier layering** inside each subdomain.

Current subdomain:
- `assetsubdomain`

Layers:
- `presentationlayer`: controllers and request/response models (DTOs)
- `businesslayer`: service interfaces and implementations (use cases and rules)
- `datalayer`: JPA entities and repositories
- `datamapperlayer`: MapStruct mappers between DTOs and entities

## Tech Stack
- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- PostgreSQL
- Jakarta Validation
- Lombok
- Flyway (database migrations)
- Gradle

Planned/next additions:
- MapStruct
- Global exception handling (`@RestControllerAdvice`)
- Unit and integration tests (Mockito, MockMvc, H2)
- React + TypeScript frontend

## Database Migrations
Flyway is enabled via dependencies in `build.gradle`.
Migration scripts should live in:
- `src/main/resources/db/migration`

Naming convention:
- `V1__create_assets.sql`
- `V2__...sql`


## Authentication and Authorization (Planned)
The platform will use OAuth2 for secure login and delegated identity, with support for providers such as Google or GitHub.
Access control will be role-based (for example: Admin, Analyst, Auditor), and API endpoints will be protected with Spring Security.
## Planned Roadmap
1. Phase 1: AssetSubdomain
2. Phase 2: RiskSubdomain
3. Phase 3: ControlSubdomain
4. Phase 4: EvidenceSubdomain
5. Phase 5: Security/AuthN/AuthZ
6. Phase 6: IAM + AppSec subdomains
7. Phase 7: Reporting and traceability endpoints
8. Phase 8: Deployment (Docker + compose) and frontend integration

## Local Run
1. Configure database in `application.yaml`
2. Run:
   - `./gradlew bootRun` (Linux/Mac)
   - `gradlew.bat bootRun` (Windows)
3. Flyway migrations execute at startup before JPA usage