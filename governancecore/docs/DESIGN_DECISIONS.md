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

## 3. Governance Core Does Not Replace the Identity Provider

### Decision

Governance Core does not try to become its own identity provider or full replacement for Keycloak.

Keycloak remains responsible for:

- authentication
- token issuance
- core identity provider behavior

Governance Core focuses on:

- access governance
- role-aware application behavior
- access lifecycle changes
- auditability
- compliance-oriented traceability

### Why

Trying to rebuild identity-provider behavior inside the application would blur responsibilities and weaken the project architecture.

The stronger design is to treat authentication and governance as separate concerns:

- the IdP confirms who the user is
- Governance Core decides how access is governed, reviewed, changed, and recorded

### Plain English explanation

Keycloak answers: "Who is this user?"

Governance Core answers: "What access should this user have, how does that access change, and how do we audit those changes?"

---

## 4. Spring Security as Resource Server

### Decision

The backend is configured as an OAuth2 Resource Server.

### Why

This allows Spring Security to:

- validate JWTs issued by Keycloak
- trust a specific issuer via `issuer-uri`
- authenticate incoming API requests
- enforce RBAC rules using Spring authorities

---

## 5. Role Mapping from Keycloak to Spring Security

### Decision

Business roles are extracted from `realm_access.roles` in the JWT and mapped to Spring Security authorities:

- `ADMIN` -> `ROLE_ADMIN`
- `ANALYST` -> `ROLE_ANALYST`
- `AUDITOR` -> `ROLE_AUDITOR`

### Why

Spring Security does not automatically understand Keycloak business roles in the way the application needs them.

Explicit role mapping ensures backend rules such as `hasRole(...)` and `hasAnyRole(...)` work correctly and ignore unrelated technical Keycloak roles.

---

## 6. Backend-First Enforcement, Frontend-Second Reflection

### Decision

Authorization rules are enforced in the backend first, then reflected in the frontend UI.

### Why

Frontend-only hiding is not security.

The backend must remain the source of truth for access control, and the frontend should reflect those rules only after backend enforcement is correct.

### Plain English explanation

If the backend does not block an action, the system is not secure.

If the frontend does not reflect the same rule, the system is confusing.

So the correct order is:

1. enforce in backend
2. reflect in frontend

---

## 7. Audit Logging Is a First-Class Backend Concern

### Decision

Audit logging will be implemented as a core backend capability before building a rich admin dashboard.

Audit events should capture at least:

- actor
- action
- target type
- target identifier
- timestamp
- outcome

### Why

Audit is not a presentation feature. It is a system behavior.

If the project wants to demonstrate IAM governance and compliance value, it must first generate trustworthy audit records before it visualizes them.

### Plain English explanation

A dashboard that shows logs is only useful if the logging model is already correct.

So the system must learn how to record important IAM and governance events before it tries to display them nicely.

---

## 8. Access Lifecycle Workflows Before Admin Dashboards

### Decision

Access lifecycle workflows are prioritized before a broad admin dashboard UI.

The core lifecycle operations are:

- assign role
- change role
- revoke role
- disable access

### Why

These workflows are closer to real IAM value than simply building management screens.

A polished dashboard without real lifecycle logic underneath would make the project look shallow.

The stronger sequence is:

1. implement lifecycle behavior
2. audit it
3. then expose it through admin pages

### Plain English explanation

The important part is not drawing an admin page.

The important part is making access change correctly over time.

---

## 9. Provisioning and Deprovisioning Start as Simulation

### Decision

Provisioning and deprovisioning will first be implemented as explicit application workflows or simulations before attempting deeper external-system integration.

Examples include:

- new user receives initial role assignment
- existing user changes role
- departing user loses access

### Why

This keeps the project realistic and teachable.

It allows the project to model Joiner / Mover / Leaver (JML) behavior clearly without prematurely taking on the complexity of full enterprise connector integration.

### Plain English explanation

First show that the system understands how access should be granted, changed, and removed.

Only after that should it worry about more advanced automation or external provisioning integrations.
