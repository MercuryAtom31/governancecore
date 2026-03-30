# Governance Core - Frontend Design Decisions

This document captures the key frontend design decisions that shape how Governance Core handles authentication, current-user state, token propagation, role-aware rendering, and domain integrity.

## 1. SPA + OIDC for the Current Phase

### Decision

Use a single-page application (SPA) with OpenID Connect (OIDC) login handled directly in the React frontend.

### Rationale

For the current phase of the project, the main goal is to understand and implement the end-to-end IAM flow clearly:

1. the user is redirected to the Identity Provider
2. the user authenticates through Keycloak
3. the frontend receives an authenticated OIDC session
4. the frontend sends the access token to the backend API
5. the backend validates the JWT and enforces RBAC

This approach keeps the authentication flow visible and easier to reason about during implementation.

### Tradeoff

A backend-for-frontend (BFF) with `httpOnly` cookies is a stronger pattern for some production systems, but it would add additional complexity immediately:

- backend-managed login/session flow
- server-issued cookies
- CSRF handling
- token exchange on the server side
- more complex logout synchronization

The project intentionally defers that model until the direct OIDC + bearer token flow is fully understood.

## 2. `/api/v1/auth/me` as the Trusted Current-User Source

### Decision

Use the backend endpoint `/api/v1/auth/me` as the trusted frontend source for the current user profile and roles.

### Rationale

The frontend should not rely on scattered token parsing for application behavior.

Instead, it should ask the backend:

- who the authenticated user is
- which roles the backend trusts for that user

This keeps frontend identity state aligned with backend authorization logic.

### Architectural Impact

The frontend stores and consumes:

- `username`
- `email`
- `roles`

from a backend-confirmed source rather than treating raw token claims as the application contract.

## 3. Centralized Token Propagation Through Axios

### Decision

Use a shared Axios client with a request interceptor that automatically attaches the current access token to API requests.

### Rationale

Without a central HTTP layer, every API call would need to attach the bearer token manually. That is error-prone and difficult to maintain.

Centralizing token propagation gives the project:

- one place to apply the `Authorization` header
- consistent authenticated API behavior
- cleaner API modules and feature code

### Architectural Impact

Frontend feature code calls the shared Axios client. The client is responsible for attaching the bearer token when an authenticated OIDC session exists.

## 4. Explicit Frontend Current-User State

### Decision

Store the current authenticated user in a dedicated React context (`CurrentUserContext`) exposed through a custom hook.

### Rationale

Authentication state and application user state are related, but they are not identical.

The OIDC layer answers:
- is there an authenticated session?

The current-user layer answers:
- who is the backend-confirmed user?
- what roles does that user have?
- is the frontend still loading that information?

Keeping that state in a shared context avoids repeated API calls and avoids prop-drilling user/role data through the component tree.

## 5. Backend Enforces, Frontend Reflects

### Decision

Treat backend authorization as the source of truth. Use frontend role checks only to reflect backend permissions in the UI.

### Rationale

Hiding a button in the UI is not security.

Real security is enforced by the backend through JWT validation and RBAC rules. The frontend role checks exist to improve usability by avoiding UI actions that the backend would reject.

### Architectural Impact

The project follows this rule consistently:

- backend enforcement provides real protection
- frontend rendering provides clarity and a better user experience

This is why role-aware rendering is described as reflection, not enforcement.

## 6. Strict `DataClassification` Union Type

### Decision

Use a TypeScript union type to restrict asset classification values to:

`PUBLIC | INTERNAL | CONFIDENTIAL | RESTRICTED`

### Rationale

This enforces valid classification values at compile time and reduces the chance of inconsistent governance data entering the UI.

It helps prevent:

- typos
- invalid classification values
- inconsistent asset handling across features
- accidental breaks in future risk and compliance logic
- drift between frontend form values and backend domain expectations

### Architectural Impact

The frontend preserves domain integrity earlier in the request lifecycle by constraining valid asset classification values before requests are sent to the backend.

## 7. Role-Aware Rendering Before Full Authorization Helpers

### Decision

Start with direct role-aware rendering in feature pages, then extract shared authorization helpers once the patterns stabilize.

### Rationale

The project already needs to reflect role differences such as:

- `ADMIN` and `ANALYST` can manage assets
- `AUDITOR` is read-only

Implementing that directly in the relevant feature page first keeps the behavior visible and easy to validate. Once multiple pages need the same logic, the role checks should move into shared frontend authorization helpers.

### Architectural Impact

This supports an incremental approach:

1. prove the RBAC behavior in a real UI flow
2. extract reusable authorization helpers once the checks repeat across the app

## Summary

The frontend is intentionally designed to:

- authenticate through OIDC
- rely on the backend for trusted current-user identity
- propagate access tokens centrally
- reflect RBAC in the UI without pretending that the UI is the security boundary
- preserve domain integrity through strict frontend typing where appropriate

These decisions keep the current architecture coherent while leaving room for later evolution toward stronger patterns such as a BFF if the project scope requires it.
