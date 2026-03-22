# Governance Core - ISMS / GRC Platform

Governance Core is an Information Security Management System (ISMS) / Governance, Risk, and Compliance (GRC) platform built with Spring Boot and React. It centralizes security governance in one system instead of spreading assets, risks, controls, and evidence across spreadsheets and documents.

## Product Vision

Organizations working toward compliance frameworks such as ISO 27001 and SOC 2 need traceability between what they protect, the risks they track, the controls they implement, and the evidence they present.

Governance Core is being built to provide that traceability in one platform:

```text
Asset -> Risk -> Control -> Evidence
```

## Who This Is For

| Role | How they use it |
|---|---|
| Security / GRC Teams | Manage governance records, map controls, track compliance status |
| Engineering Managers | Own systems and assets, track remediation and accountability |
| Auditors / Compliance | Review evidence, verify controls, inspect read-only records |

## Current Status

**Current vertical slice: AssetSubdomain + Security Foundation**

Implemented backend capabilities:
- Create an asset
- List all assets
- Get asset by ID
- Update an asset
- Delete an asset

Implemented security capabilities:
- Spring Security configured as an OAuth2 Resource Server
- JWT validation through Keycloak issuer metadata
- Role extraction from `realm_access.roles`
- RBAC roles: `ADMIN`, `ANALYST`, `AUDITOR`
- `/api/v1/auth/me` endpoint returning authenticated user info
- Role-based backend rules on asset endpoints

Implemented frontend capabilities:
- React + TypeScript asset list/create flow
- OIDC login redirect with Keycloak
- Auth provider and auth gate in the frontend
- Frontend sign-out flow

## Architecture

### Backend

The backend is organized by subdomain, with n-tier layering inside each subdomain:

```text
assetsubdomain/
  presentationlayer/    -> REST controllers + request/response models
  businesslayer/        -> service interfaces + use-case implementations
  datalayer/            -> JPA entities + repositories
  datamapperlayer/      -> mapping between entities and API models

authsubdomain/
  presentationlayer/    -> authenticated user endpoints (`/api/v1/auth/me`)

config/
  SecurityConfig.java   -> Spring Security + JWT + RBAC configuration
  CorsConfig.java       -> CORS rules for frontend/backend communication

utils/
  GlobalControllerExceptionHandler.java
  HttpErrorInfo.java
  exceptions/
```

**Subdomain boundary rule:** subdomains reference each other by ID instead of importing each other's entities directly.

### Frontend

The frontend follows a feature-based structure with separation of concerns:

```text
src/
  features/
    assets/
      api/              -> backend HTTP calls
      hooks/            -> state + side effects
      pages/            -> route-level UI
      components/       -> feature UI pieces
      types/            -> feature contracts
  auth/
    oidcConfig.ts       -> OIDC client configuration
    AuthGate.tsx        -> redirects unauthenticated users to login
  lib/
    axios.ts            -> shared HTTP client
  ui/                   -> reusable UI primitives
```

## Tech Stack

### Backend

| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot 3.5.x | Application framework |
| Spring Web | REST API layer |
| Spring Data JPA | Database access |
| Spring Security | API protection and RBAC |
| OAuth2 Resource Server | JWT validation for protected endpoints |
| Flyway | Database schema migrations |
| H2 / PostgreSQL | Development and production databases |
| Jakarta Validation | Request validation |
| Lombok | Boilerplate reduction |
| Gradle | Build tool |

### Frontend

| Technology | Purpose |
|---|---|
| React | Component-based UI |
| TypeScript | Type-safe frontend development |
| React Router | Routing |
| Axios | HTTP client |
| react-oidc-context | OIDC login flow integration |
| oidc-client-ts | OIDC client support |
| CSS | Styling |

### Testing

| Technology | Purpose |
|---|---|
| JUnit 5 | Test framework |
| Mockito | Service unit tests |
| MockMvc | Controller/web tests |
| H2 | In-memory test database |

## Security Design

Governance Core uses **Keycloak** as the local Identity Provider (IdP).

### What that means

- Keycloak confirms **who the user is**
- Spring Security decides **what the user is allowed to do**
- The backend trusts JWTs issued by the `governance-core` realm
- The frontend uses OIDC to redirect users to Keycloak for login

### Roles

- `ADMIN` - full platform access
- `ANALYST` - can create, update, and manage governance records
- `AUDITOR` - read-only access for verification and review

### Current backend RBAC rules

- `GET /api/v1/assets/**` -> `ADMIN`, `ANALYST`, `AUDITOR`
- `POST /api/v1/assets/**` -> `ADMIN`, `ANALYST`
- `PUT /api/v1/assets/**` -> `ADMIN`, `ANALYST`
- `DELETE /api/v1/assets/**` -> `ADMIN`, `ANALYST`
- `GET /api/v1/auth/me` -> any authenticated user

## Local Development Setup

### Prerequisites

- Java 21
- Node.js + npm
- Docker

## Run Keycloak locally

Start Keycloak with persistent storage:

```powershell
docker run -p 8081:8080 `
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin `
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin `
  -v keycloak_data:/opt/keycloak/data `
  quay.io/keycloak/keycloak:latest start-dev
```

Keycloak will be available at:

```text
http://localhost:8081
```

### Required local Keycloak setup

Realm:
- `governance-core`

Realm roles:
- `ADMIN`
- `ANALYST`
- `AUDITOR`

Test users:
- `admin.user`
- `analyst.user`
- `auditor.user`

Frontend client:
- `governancecore-frontend`

Key frontend client settings:
- Client type: `OpenID Connect`
- Client authentication: `Off`
- Standard flow: `On`
- PKCE method: `S256`
- Root URL: `http://localhost:5173`
- Home URL: `http://localhost:5173`
- Valid redirect URIs: `http://localhost:5173/*`
- Valid post logout redirect URIs: `http://localhost:5173/*`
- Web origins: `http://localhost:5173`

## Backend configuration

The backend trusts JWTs from Keycloak using the issuer URI in:

- `governancecore/src/main/resources/application.yaml`

Relevant setting:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081/realms/governance-core
```

## Run the backend

```powershell
cd governancecore
.\gradlew.bat bootRun --args='--spring.profiles.active=local'
```

## Run the frontend

```powershell
cd governancecore-frontend
npm install
npm run dev
```

The frontend will be available at:

```text
http://localhost:5173
```

## Authentication Flow

Current login flow:

1. User opens the React frontend
2. The frontend auth gate checks whether the user is authenticated
3. If not, the frontend redirects the user to Keycloak
4. Keycloak authenticates the user and issues tokens
5. Keycloak redirects the user back to the frontend
6. The frontend uses the authenticated session to call the protected backend
7. Spring Security validates the JWT and applies RBAC rules

## Useful Endpoints

- `GET /api/v1/assets`
- `POST /api/v1/assets`
- `GET /api/v1/auth/me`

## Roadmap

| Phase | Area | Description |
|---|---|---|
| 1 | AssetSubdomain | Asset inventory foundation |
| 2 | Security Foundation | Keycloak + Spring Security + OIDC frontend login |
| 3 | RiskSubdomain | Risk register and scoring |
| 4 | ControlSubdomain | Control catalog and traceability |
| 5 | EvidenceSubdomain | Evidence records and linkage |
| 6 | IAMSubdomain | Access assignments, reviews, and workflows |
| 7 | Reporting | Audit-ready traceability reports |
| 8 | Deployment | Containerized local and deployment setup |

## Project Status

Active development.
