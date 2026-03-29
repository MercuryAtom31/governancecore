# Security Foundation - Phase 1

This document defines the first security implementation phase for Governance Core.

The goal of this phase is not to build the full IAM governance domain yet.
The goal is to establish the authentication and authorization foundation that the rest of the platform will rely on.

This means:
- delegated authentication through a trusted identity provider
- JWT-based API protection
- role-based authorization
- role-aware frontend behavior
- one secured vertical slice applied first to the Asset feature

---

## 1. Phase Objective

Phase 1 introduces the security base layer for the platform.

By the end of this phase, the system should support:
- login through a trusted identity provider
- validated JWTs on the backend
- role resolution for authenticated users
- protected backend endpoints
- role-aware frontend behavior
- a trusted `/api/v1/auth/me` endpoint for current-user information

This phase is intentionally limited.

It does **not** yet include:
- full IAM lifecycle workflows
- access review campaigns
- rich user administration UI
- privileged access attestation workflows
- multi-tenant identity boundaries
- advanced policy engines
- complete provisioning integrations

Those belong to later phases.

---

## 2. Security Scope for This Phase

This phase covers two security layers.

### Authentication
Authentication answers:

> Who is the user?

Governance Core will **not** store passwords.
Authentication is delegated to a trusted identity provider.

### Authorization
Authorization answers:

> What is this user allowed to do?

The platform will enforce authorization using roles and route/method-level access rules.

### Plain English explanation

This phase establishes the security boundary of the system.

It makes sure the platform knows who the user is and what that user is allowed to do before more advanced IAM workflows are added.

---

## 3. Identity Provider Strategy

### Identity Provider
**Keycloak**

### Protocols
- **OIDC** for identity
- **OAuth2** for delegated authorization flow

### Token Type
- **JWT**

### Why Keycloak?
Keycloak is chosen for local development and IAM learning because it allows the project to practice:
- OAuth2 / OIDC concepts
- JWT validation
- real role claims
- local users and role assignment
- admin / analyst / auditor testing without external dependencies

This is more appropriate for deep IAM learning than starting only with Google or GitHub login.

---

## 4. Role Model

Phase 1 uses the role model already defined in the product documentation.

### Roles
- `ADMIN`
- `ANALYST`
- `AUDITOR`

### Role Intent

#### ADMIN
Purpose:
- platform oversight
- governance authority
- full access to administrative functions

#### ANALYST
Purpose:
- primary active operational role
- create and manage governance records

#### AUDITOR
Purpose:
- independent read-only verification

---

## 5. Asset Feature Authorization Policy

The Asset feature becomes the first secured vertical slice.

### Backend Policy

#### Read Access
`GET /api/v1/assets/**`
- allowed to: `ADMIN`, `ANALYST`, `AUDITOR`
- rule: any authenticated role

#### Create Access
`POST /api/v1/assets/**`
- allowed to: `ADMIN`, `ANALYST`
- denied to: `AUDITOR`

#### Update Access
`PUT /api/v1/assets/**`
- allowed to: `ADMIN`, `ANALYST`
- denied to: `AUDITOR`

#### Delete Access
`DELETE /api/v1/assets/**`
- allowed to: `ADMIN`, `ANALYST`
- denied to: `AUDITOR`

### Frontend Policy

#### Auditor
- read-only access
- can view assets
- cannot see add/edit/delete actions

#### Analyst / Admin
- can view assets
- can create assets
- can later edit/delete assets

Important:
Frontend restrictions improve UX, but the **backend remains the real security boundary**.

---

## 6. Architecture Decision

Security is introduced in this order:

1. define the rules
2. build backend security foundation
3. connect real identity provider
4. expose authenticated user info
5. secure the first feature
6. reflect role rules in the frontend

This avoids implementing UI behavior before real backend enforcement.

---

## 7. Backend Foundation Implemented in This Phase

### Dependencies
The backend adds:
- `spring-boot-starter-security`
- `spring-boot-starter-oauth2-resource-server`

### Why?
Because Governance Core should behave as a **resource server**.

That means:
- the frontend sends a JWT
- Spring Security validates the JWT
- Spring resolves the user identity and authorities
- the application enforces role rules on protected endpoints

---

## 8. Core Backend Security Components

### 8.1 Security Configuration
`SecurityConfig.java` defines:
- which routes are public
- which routes require authentication
- how JWT authentication is enabled
- how CORS is handled under Spring Security
- how role-based access rules are enforced

Phase 1 baseline:
- `/api/v1/auth/me` -> authenticated
- `/api/v1/assets/**` -> role-aware access rules
- future routes will be added gradually

---

### 8.2 JWT Resource Server Configuration
Resource server settings are added in application configuration, including the identity provider issuer URI.

Purpose:
- Spring Boot does not invent identity
- it trusts tokens issued by a configured identity provider
- token validation becomes declarative and standards-based

---

### 8.3 JWT Authority Mapping
A JWT does not automatically become Spring roles the way the application needs.
The backend explicitly maps token claims into authorities like:
- `ROLE_ADMIN`
- `ROLE_ANALYST`
- `ROLE_AUDITOR`

This is necessary so expressions like:
- `hasRole('ADMIN')`
- `@PreAuthorize("hasRole('ANALYST')")`

work correctly.

This is one of the most important IAM learning points in the backend.

---

### 8.4 `/api/v1/auth/me`
Phase 1 includes the endpoint:

`GET /api/v1/auth/me`

This endpoint returns trusted current-user data such as:
- username
- email
- roles

Purpose:
- confirm authentication works
- confirm token parsing works
- confirm role extraction works
- provide a trusted bridge from backend security to frontend UI logic

---

### 8.5 Asset Endpoint Protection
After the security foundation is working, role rules are applied to the Asset feature.

This can be enforced through:
- route security rules
- method security
- `@PreAuthorize`

Recommended model:
- authenticated access for all read operations
- role checks for write operations

This is where the project starts practicing real authorization and least privilege.

---

## 9. Identity Provider Setup Order

Keycloak should be introduced early enough to ground the design in a real token model.

### Keycloak Setup Tasks
- create realm
- create frontend client
- create backend API client if needed for separation
- define roles: `ADMIN`, `ANALYST`, `AUDITOR`
- create test users
- assign roles
- inspect real JWT claims

Why inspect the token early?
Because backend role mapping should be based on real token structure, not guesses.

---

## 10. Frontend Security Foundation

The frontend should be secured **after** backend enforcement exists.

### Frontend Tasks in Phase 1
- create OIDC login setup
- create current-user provider/state
- call `/api/v1/auth/me`
- store current user identity and roles
- protect routes
- conditionally hide or disable write actions for auditors

### Important Principle
Frontend role checks are not the source of truth.
They are a UI reflection of backend security policy.

The backend is the actual enforcement boundary.

---

## 11. Exact Implementation Order for Phase 1

### Step 1 - Documentation
Create and maintain this file:
- `docs/security-foundation-phase-1.md`

### Step 2 - Backend Dependencies
Add:
- Spring Security
- OAuth2 Resource Server

### Step 3 - Local Identity Provider
Run local Keycloak and define:
- realm
- client
- roles
- test users

### Step 4 - Token Inspection
Inspect a real Keycloak token and identify:
- username claim
- email claim
- role claim structure

### Step 5 - Backend Security Config
Create:
- `SecurityConfig.java`

### Step 6 - JWT Role Mapping
Create JWT-to-authority conversion logic.

### Step 7 - Application Configuration
Add issuer URI and JWT settings.

### Step 8 - Current User Endpoint
Add:
- `/api/v1/auth/me`

### Step 9 - Secure Assets Feature
Apply role rules to current asset endpoints.

### Step 10 - Frontend Auth State
Add:
- OIDC auth provider
- current user loading
- role-aware rendering

---

## 12. What Is Being Learned at Each Step

### Documentation Step
Learn:
- permission modeling
- role design
- security requirements before implementation

### Spring Security Step
Learn:
- what a protected API is
- what a resource server is
- how JWT-based backend protection works

### Keycloak + Token Step
Learn:
- identity provider architecture
- OIDC / OAuth2 token structure
- real-world role claims

### Authority Mapping Step
Learn:
- how identity claims become application roles
- how Spring authorization actually works internally

### `/auth/me` Step
Learn:
- how a backend exposes identity safely to the UI
- how frontend and backend security connect

### Asset Protection Step
Learn:
- authentication vs authorization
- least privilege
- read-only vs write-capable roles

### Frontend Auth Step
Learn:
- how UI reflects backend-enforced security rules
- role-aware rendering without pretending the UI is the security boundary

---

## 13. What This Phase Does Not Yet Build

This phase intentionally does not yet implement the deeper IAM governance workflows such as:
- access assignment records as domain entities
- access request workflow
- quarterly access review workflow
- manager approvals
- privileged access attestations
- full user administration screens inside Governance Core
- identity lifecycle automation
- provisioning connectors to external systems
- rich audit dashboards

These remain important, but they depend on the security foundation established here.

---

## 14. What Comes Immediately After Phase 1

Once the security foundation is in place, the next implementation priorities are:

1. audit logging as a backend capability
2. access lifecycle workflows
   - assign role
   - change role
   - revoke role
   - disable access
3. stronger frontend role-aware behavior
4. provisioning / deprovisioning simulation
5. richer admin and audit UI built on top of real backend workflows

### Plain English explanation

Phase 1 teaches the system how to authenticate users and enforce roles.

The next phase teaches the system how access changes over time and how those changes are recorded.

---

## 15. Final Summary

Phase 1 security is the identity and authorization base layer of Governance Core.

It introduces:
- delegated authentication
- JWT validation
- role-based access control
- a trusted current-user endpoint
- role-aware frontend behavior
- the first secured feature: Assets

The purpose of this phase is to ensure that future access-governance and audit workflows are built on top of real security boundaries from the beginning, not retrofitted at the end.
