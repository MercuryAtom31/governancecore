# Governance Core - Design Decisions

This document explains key architectural and security decisions.

---

## 1. Authentication Architecture: SPA + OIDC + Bearer Token First

### Decision

For the current phase of the project, Governance Core uses:

- Keycloak as the Identity Provider (IdP)
- OIDC login in the React frontend
- JWT bearer token validation in the Spring Boot backend
- Spring Security configured as an OAuth2 Resource Server

The frontend authenticates the user through Keycloak and then sends the access token to the backend API in the `Authorization: Bearer <token>` header.

### Why this decision was made

At this stage, the goal is to learn and implement the core IAM flow clearly and incrementally.

That core flow is:

1. the Identity Provider authenticates the user
2. the frontend obtains an authenticated session
3. the frontend sends the access token to the backend
4. the backend validates the JWT
5. the backend extracts roles from the token
6. the backend enforces RBAC rules

This architecture makes each step visible and easier to understand.

### Why we did NOT start with a BFF + httpOnly cookie design

A BFF (backend-for-frontend) with `httpOnly` cookies is a stronger security pattern in many production systems, but it also changes the architecture significantly.

A BFF approach would require introducing additional concerns immediately:

- backend-managed login/session flow
- server-issued cookies
- CSRF protection
- session lifecycle management
- token exchange on the backend
- frontend no longer working directly with bearer tokens

That is a valid future direction, but it would make the current learning phase much harder to follow.

### Plain English explanation

We chose the simpler, standard SPA + OIDC + bearer-token flow first so that the identity flow is easy to observe and understand.

In plain English:

- Keycloak confirms who the user is
- the frontend receives the authenticated user session
- the frontend sends the token to the backend
- the backend decides what that user is allowed to do

### Tradeoff acknowledged

This current approach is acceptable for learning and for many SPA designs, but browser-accessible storage is not the strongest possible security model.

Storing OIDC user/session data in browser storage means JavaScript can access it. That increases exposure if the application ever suffers from a serious XSS vulnerability.

For that reason:

- this choice is intentional for the current learning and implementation phase
- a future hardened version of the project may evolve toward a BFF + `httpOnly` cookie model

### Future evolution

Once the current IAM foundation is complete, the project can later compare or migrate toward:

- BFF-based authentication flow
- `httpOnly`, secure, same-site cookies
- reduced token exposure in the browser
- stronger separation between frontend session state and backend token handling

That later phase will make more sense after the current architecture is fully implemented and understood.

---

## 2. Keycloak as Identity Provider

### Decision

Keycloak is used locally as the Identity Provider.

### Why

Keycloak gives the project a real IAM environment with:

- realms
- users
- roles
- OIDC/OAuth2 flows
- JWT issuance

This is more educational and more aligned with enterprise IAM concepts than using a simple social login provider at this stage.

---

## 3. Spring Security as Resource Server

### Decision

The backend is configured as an OAuth2 Resource Server.

### Why

This allows Spring Security to:

- validate JWTs issued by Keycloak
- trust a specific issuer via `issuer-uri`
- authenticate incoming API requests
- enforce RBAC rules using Spring authorities

---

## 4. Role Mapping from Keycloak to Spring Security

### Decision

Business roles are extracted from `realm_access.roles` in the JWT and mapped to Spring Security authorities:

- `ADMIN` -> `ROLE_ADMIN`
- `ANALYST` -> `ROLE_ANALYST`
- `AUDITOR` -> `ROLE_AUDITOR`

### Why

Spring Security does not automatically understand Keycloak business roles in the way the application needs them.

Explicit role mapping ensures backend rules such as `hasRole(...)` and `hasAnyRole(...)` work correctly and ignore unrelated technical Keycloak roles.

---

## 5. Backend-First Enforcement, Frontend-Second Reflection

### Decision

Authorization rules are enforced in the backend first, then reflected in the frontend UI.

### Why

Frontend-only hiding is not security.

The backend must remain the source of truth for access control, and the frontend should reflect those rules only after backend enforcement is correct.
