# Security Foundation - Phase 1

This document defines the first security implementation phase for Governance Core.

The goal of this phase is not to build the full IAM subdomain yet.
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
- full IAM workflows
- access review campaigns
- user administration UI
- privileged access attestation workflows
- multi-tenant identity boundaries
- advanced policy engines

Those belong to later phases.

---

## 2. Security Scope for This Phase

This phase covers two security layers:

### Authentication
Authentication answers:

> Who is the user?

Governance Core will **not** store passwords.
Authentication is delegated to a trusted identity provider.

### Authorization
Authorization answers:

> What is this user allowed to do?

The platform will enforce authorization using roles and route/method-level access rules.

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
- full access

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

Security will be introduced in this order:

1. define the rules
2. build backend security foundation
3. connect real identity provider
4. expose authenticated user info
5. secure the first feature
6. reflect role rules in the frontend

This avoids implementing UI behavior before real backend enforcement.

---

## 7. Backend Foundation to Implement

### Dependencies
The backend must add:
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
Create:

`src/main/java/com/benzair/governancecore/config/SecurityConfig.java`

This class will define:
- which routes are public
- which routes require authentication
- how JWT authentication is enabled
- how CORS is handled under Spring Security
- how role-based access rules are enforced

Initial baseline:
- `/api/v1/auth/me` -> authenticated
- `/api/v1/assets/**` -> authenticated
- future routes will be added gradually

---

### 8.2 JWT Resource Server Configuration
Add resource server settings in:
- `application.yaml`
- or profile-specific environment settings if needed

This will include the identity provider issuer URI.

Purpose:
- Spring Boot does not invent identity
- it trusts tokens issued by a configured identity provider
- token validation becomes declarative and standards-based

---

### 8.3 JWT Authority Mapping
This is a required step.

A JWT does not automatically become Spring roles the way the application needs.
The backend must explicitly map token claims into Spring authorities.

This means creating a converter that maps identity-provider role claims into authorities like:
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
Add the endpoint:

`GET /api/v1/auth/me`

This endpoint should return trusted current-user data such as:
- username
- email
- roles

Purpose:
- confirm authentication works
- confirm token parsing works
- confirm role extraction works
- provide a trusted bridge from backend security to frontend UI logic

This endpoint is the first security-aware API endpoint and should be implemented early.

---

### 8.5 Asset Endpoint Protection
After the security foundation is working, apply role rules to the Asset feature.

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

### Frontend Tasks
- create `AuthContext` or equivalent auth state provider
- call `/api/v1/auth/me`
- store current user identity and roles
- create role helpers
- protect routes
- conditionally hide or disable write actions for auditors

### Important Principle
Frontend role checks are not the source of truth.
They are a UI reflection of backend security policy.

The backend is the actual enforcement boundary.

---

## 11. Exact Implementation Order

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
- auth context
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

This phase intentionally does not yet implement the IAM subdomain workflows such as:
- access assignment records
- access request workflow
- quarterly access review workflow
- manager approvals
- privileged access attestations
- user administration screens
- identity lifecycle automation

These remain important, but they depend on the security foundation established here.

---

## 14. Final Summary

Phase 1 security is the identity and authorization base layer of Governance Core.

It introduces:
- delegated authentication
- JWT validation
- role-based access control
- a trusted current-user endpoint
- role-aware frontend behavior
- the first secured feature: Assets

The purpose of this phase is to ensure that future subdomains are built on top of real security boundaries from the beginning, not retrofitted at the end.

---

## 15. Immediate Next Coding Target

The next coding target after this document is:

1. add Spring Security dependencies
2. run local Keycloak
3. inspect token claims
4. create `SecurityConfig.java`

That is the correct foundation for the next phase of the project.
