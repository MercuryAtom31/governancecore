# Governance Core - UI Architecture

This document explains how the frontend is structured and how the UI reflects the platform's security and governance model.

The UI is not treated as a separate decorative layer.
It is an application surface that should make governance, role boundaries, and current system state understandable without weakening the backend security model.

---

## 1. UI Philosophy

Governance Core is a governance-oriented security platform, not a generic CRUD application.

The UI should communicate:

- structural clarity
- trust
- role boundaries
- current governance state
- system seriousness without visual noise

Primary objective:

> A user should be able to log in and quickly understand both the security posture of the platform and what actions their role allows them to perform.

The interface should feel:

- calm
- deliberate
- readable
- operational
- role-aware

---

## 2. Current Frontend Architecture

The frontend currently follows this sequence:

1. user reaches the React application
2. `AuthGate` checks whether the user is authenticated through OIDC
3. if not authenticated, the user is redirected to Keycloak
4. after successful login, the frontend stores the authenticated OIDC session
5. Axios attaches the access token to backend API calls
6. the frontend calls `/api/v1/auth/me`
7. `CurrentUserContext` stores the trusted current user profile
8. pages render according to the backend-confirmed user role

### Plain English explanation

The frontend does not decide who the user is by itself.

It relies on:
- Keycloak for authentication
- the backend for trusted current-user identity and roles

That keeps the UI aligned with real backend security instead of guessing from raw token data everywhere.

---

## 3. Global Application Structure

### Current Composition

The app shell currently contains:

- top navigation area
- main routed content area
- footer/signature area

Security and identity are layered around it through:

- `AuthProvider` from the OIDC client library
- `AuthGate`
- `CurrentUserProvider`

### Why this structure was chosen

The goal is to keep authentication and user-state concerns near the app shell so that feature pages do not each reinvent login, token handling, or current-user loading.

---

## 4. Authentication and Session Flow in the UI

### OIDC Login

The frontend uses OIDC against Keycloak.

The frontend is responsible for:
- starting the login flow
- handling redirect return after login
- maintaining authenticated session state
- triggering sign-out

The frontend is **not** responsible for:
- validating JWT signatures
- deciding which roles are trusted
- enforcing security boundaries on the API

That remains backend responsibility.

### Auth Gate

`AuthGate` protects the application shell.

Behavior:
- if the user is not authenticated, redirect to Keycloak
- if the user is authenticated, render the app

This keeps protected pages from acting like public pages.

---

## 5. Current User State

### Trusted Current User Source

The frontend uses:

`GET /api/v1/auth/me`

This endpoint returns:
- username
- email
- roles

### Why `/auth/me` matters

The frontend already has an OIDC session, but it still needs a trusted application-level user model.

That is why the frontend does not rely only on raw token claims scattered across the UI.

Instead, it loads one backend-confirmed user profile and stores it in a shared React context.

### Current User Context

`CurrentUserContext` centralizes:
- `currentUser`
- `loading`
- `error`
- refresh capability

This means feature pages can consume current-user state without repeating API calls or auth logic.

### Plain English explanation

The frontend now knows not only that a user logged in, but also which user the backend recognizes and which roles the backend trusts.

---

## 6. Token Propagation and API Access

The frontend uses a shared Axios client.

That client automatically:
- reads the current OIDC access token
- attaches it to outgoing backend requests as:
  - `Authorization: Bearer <token>`

### Why this matters

A user being "logged in" in the frontend is not enough.

The backend only recognizes the user when the token is actually attached to the request.

This is why token propagation is a core part of the UI architecture, not a minor implementation detail.

---

## 7. Role-Aware Rendering Model

### Current Principle

The frontend reflects permission rules already enforced by the backend.

This means:
- backend enforcement is the source of truth
- frontend role checks exist to improve clarity and usability
- hidden buttons are not security controls by themselves

### Current Example

On the Assets page:
- `ADMIN` and `ANALYST` can see the asset creation form
- `AUDITOR` receives a read-only experience instead

This keeps the UI consistent with backend RBAC.

### Why this matters

If the backend blocks an action but the UI still invites the user to perform it, the product feels poorly governed.

A role-aware UI reduces confusion and better communicates access boundaries.

---

## 8. Present Layout and Feature Surface

### Assets Page

The current secured feature slice is the Assets page.

Its responsibilities include:
- viewing assets
- creating assets for write-capable roles
- showing a read-only state for auditors

This page is currently the main place where frontend authorization reflection is visible.

### Future Pages

As more subdomains are added, the same model should continue:
- backend protects the operation
- frontend reflects the role state
- current-user data comes from shared context
- repeated role logic should eventually move into shared authorization helpers

---

## 9. Design System Direction

### Visual Tone

The frontend currently uses a dark interface with strong contrast and neon-accent styling.

The design direction should still preserve:
- readability
- consistent spacing
- clear hierarchy
- deliberate emphasis rather than decorative overload

### Interaction Principle

Animations and effects may exist, but they should not interfere with the operational clarity of the product.

Security and governance products should still feel stable and understandable first.

---

## 10. Frontend Security Principles

The frontend is designed around these rules:

- do not treat frontend hiding as real security
- trust backend-confirmed user state over ad hoc token parsing in feature pages
- keep auth/session logic centralized
- keep token attachment centralized in the HTTP client
- keep role-aware UI aligned with backend RBAC

---

## 11. Immediate Next Frontend Evolution

The next frontend architecture improvements should be:

1. shared authorization helpers
   - `hasRole(...)`
   - `canManageAssets(...)`
   - `isReadOnlyUser(...)`
2. broader role-aware rendering across future pages
3. audit and lifecycle UI built on top of real backend workflows
4. eventually stronger comparison or migration toward a BFF + `httpOnly` cookie model if the project reaches that hardening phase

---

## 12. Architectural Symmetry

The frontend should continue mirroring backend concerns cleanly.

Current symmetry:
- Keycloak / OIDC -> frontend authentication flow
- Spring Security / JWT validation -> trusted backend identity
- `/auth/me` -> frontend current-user model
- backend RBAC -> frontend role-aware rendering
- governance workflows -> future page and dashboard structure

This symmetry keeps the system understandable across layers.

---

## 13. Final Objective

When a user opens Governance Core, the UI should help them understand:

- who they are in the system
- what their role allows
- what assets and governance records exist
- what actions are available or unavailable to them
- how the system communicates security posture and accountability

The UI exists to make governance visible, structured, and role-aware without pretending to be the true security boundary.
