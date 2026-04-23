# Governance Core

Governance Core is a governance-oriented platform for managing security and compliance data with IAM-aware access control.

It is being built as a practical system that brings together:

- governed assets
- security and compliance workflows
- role-based access control
- identity-aware frontend and backend behavior
- future access lifecycle, PIM (Privileged Identity Management), and audit capabilities

It does **not** try to replace an Identity Provider. Instead, it uses **Keycloak** for authentication and focuses its own domain on governance, authorization, lifecycle changes, and auditability.

## What Problem It Solves

Many organizations still manage governance and compliance work across disconnected spreadsheets, documents, and ticketing workflows.

That creates problems such as:

- weak traceability between systems, risks, controls, and evidence
- unclear ownership and accountability
- inconsistent access control
- poor visibility into who can do what
- limited auditability of security and governance changes

Governance Core is being built to centralize that work into one coherent system.

## Core Idea

The project is designed around the following governance chain:

```text
Asset -> Risk -> Control -> Evidence
```

That chain will eventually sit alongside access governance concerns such as:

- who has access
- why they have access
- when that access changes
- how those changes are recorded and reviewed

## System Scope

Governance Core currently consists of:

- a **Spring Boot backend**
- a **React frontend**
- **Keycloak** as the external Identity Provider (IdP)

### Responsibility Split

**Keycloak** is responsible for:
- authenticating the user
- issuing tokens
- holding realm users and roles

**Governance Core** is responsible for:
- enforcing role-based access in the application
- exposing trusted current-user data to the frontend
- reflecting permissions in the UI
- evolving toward access lifecycle, PIM, and audit workflows

This boundary is intentional. Governance Core is an IAM-aware governance application, not a replacement for the IdP.

## Current Status

The project currently includes a working vertical slice across:

- asset management
- backend security
- frontend authentication
- role-aware UI behavior

### Backend capabilities implemented

- Create an asset
- List all assets
- Get asset by ID
- Update an asset
- Delete an asset

### Security capabilities implemented

- Spring Security configured as an OAuth2 Resource Server
- JWT validation through Keycloak issuer metadata
- Role extraction from `realm_access.roles`
- RBAC roles: `ADMIN`, `ANALYST`, `AUDITOR`
- `/api/v1/auth/me` endpoint returning authenticated user info
- Role-based backend rules on asset endpoints

### Frontend capabilities implemented

- React + TypeScript asset list/create flow
- OIDC login redirect with Keycloak
- Auth provider and auth gate in the frontend
- Access token propagation through a shared Axios client
- Backend-confirmed current-user state in the frontend
- Role-aware rendering on the asset page

## How The Pieces Fit Together

### Authentication and Authorization Flow

The current end-to-end IAM flow works like this:

1. the user opens the React frontend
2. the frontend checks whether an authenticated OIDC session exists
3. if not, the user is redirected to Keycloak
4. the user authenticates through the `governance-core` realm
5. Keycloak redirects the user back to the frontend
6. the frontend sends the access token to the backend API
7. Spring Security validates the JWT
8. the backend extracts roles and enforces RBAC
9. the frontend calls `/api/v1/auth/me` to get trusted user identity and roles
10. the UI reflects what the user is allowed to do

In plain English:
- Keycloak confirms who the user is
- Spring Security decides what the user can do
- the frontend reflects those permissions in the user experience

## Architecture Summary

### Backend

The backend is organized by subdomain, with layered responsibilities inside each subdomain.

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

The frontend follows a feature-based structure with separate areas for auth, API access, and feature-level UI.

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
    CurrentUserContext.tsx
  lib/
    axios.ts            -> shared HTTP client
  ui/                   -> reusable UI primitives
```

## Roles

The application currently recognizes three business roles:

- `ADMIN` - full platform access
- `ANALYST` - can create, update, and manage governance records
- `AUDITOR` - read-only access for verification and review

### Current backend RBAC rules

- `GET /api/v1/assets/**` -> `ADMIN`, `ANALYST`, `AUDITOR`
- `POST /api/v1/assets/**` -> `ADMIN`, `ANALYST`
- `PUT /api/v1/assets/**` -> `ADMIN`, `ANALYST`
- `DELETE /api/v1/assets/**` -> `ADMIN`, `ANALYST`
- `GET /api/v1/auth/me` -> any authenticated user

### Current frontend RBAC reflection

- `ADMIN` and `ANALYST` can see asset creation UI
- `AUDITOR` gets a read-only experience on the asset page

The backend remains the security boundary. The frontend reflects permissions for clarity and usability.

## Privileged Identity Management (PIM)

The next IAM-focused feature I want to build is a simplified Privileged Identity Management (PIM) flow.

This will let an `ANALYST` request temporary `ADMIN` access, let an `ADMIN` approve or reject the request, and grant time-limited elevated access for a short duration.

### Planned PIM flow

- analyst requests `ADMIN` access from their profile page
- admin reviews pending requests
- admin approves or rejects the request
- approved access is time-limited and expires automatically
- every step is written to the audit trail

This feature is intended to demonstrate:

- approval workflows
- Just-In-Time (JIT) access
- least privilege
- access expiry and revocation
- governance-grade auditability

## Technology Stack

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

## Local Development Setup

### Prerequisites

- Java 21
- Node.js + npm
- Docker

### Run Keycloak locally

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

### Backend configuration

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

### Run the backend

#### Option A - Persistent PostgreSQL mode (recommended)

Set environment variables (PowerShell):

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/governancecore"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
```

Then run:

```powershell
cd governancecore
.\gradlew.bat bootRun
```

#### Option B - Local in-memory H2 mode (resets on restart)

```powershell
cd governancecore
.\gradlew.bat bootRun --args='--spring.profiles.active=local'
```

Note: `local` uses in-memory H2 with `create-drop`, so created data disappears when the app restarts.

### Run the frontend

```powershell
cd governancecore-frontend
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## Documentation Map

Use this README as the high-level project overview.

Use the docs below for deeper design context:

### Backend / IAM / governance docs

- `governancecore/docs/DESIGN_DECISIONS.md`
  - project boundaries, IAM scope, sequencing decisions
- `governancecore/docs/security-foundation-phase-1.md`
  - current authentication and authorization foundation
- `governancecore/docs/user-workflows-&-role-model.md`
  - roles, access lifecycle direction, and JML framing
- `governancecore/docs/compliance-framework-mapping.md`
  - control and compliance alignment

### Frontend docs

- `governancecore-frontend/docs/ui-architecture.md`
  - frontend structure and auth/UI flow
- `governancecore-frontend/docs/design-decisions-frontend.md`
  - frontend architectural decisions and tradeoffs

## Roadmap

The planned evolution of the project is:

1. finish the security and role-aware UI foundation cleanly
2. pause the audit subdomain for the moment and implement PIM access requests and approvals
3. add time-limited privileged access and automatic revocation
4. return to audit logging for security and governance actions
5. simulate provisioning and deprovisioning flows
6. add richer admin and audit views after backend workflows exist
7. extend the broader governance domain with risks, controls, evidence, and reporting

## Project Status

Active development.
