# Governance Core - User Workflows & Role Model

This document defines:

- the role model (`ADMIN`, `ANALYST`, `AUDITOR`)
- the main governance workflows of the platform
- the access lifecycle workflows that make the system look like real IAM
- how identity, authorization, governance, and auditability connect together

This is the operational blueprint of the platform.

---

# 1. MVP Objective

## Core Goal

> A user logs in and immediately understands the organization's security posture, while the system clearly governs who can do what, how access changes, and how those changes are recorded.

Security posture is derived from structured workflows:

Asset -> Risk -> Control -> Evidence -> Dashboard

Access governance is layered on top of those workflows:

Identity -> Role -> Access Decision -> Audit Event

The platform centralizes these elements into a traceable governance engine.

---

# 2. Authentication & Identity Boundary

## Authentication Model

Users authenticate through Keycloak using OIDC.

The platform:
- does NOT store passwords
- delegates authentication to a trusted identity provider
- validates JWTs in the backend
- resolves the user's role and permissions after authentication

## Identity Boundary

Keycloak is responsible for:
- authentication
- token issuance
- identity-provider behavior

Governance Core is responsible for:
- application authorization
- access lifecycle changes
- access governance workflows
- audit logging of important actions

## Identity Concepts Practiced

- OAuth2 / OIDC
- token validation
- role-based access control (RBAC)
- privileged role enforcement
- audit logging of identity and governance events
- access lifecycle thinking (Joiner / Mover / Leaver)

---

# 3. Role Model

The platform implements strict RBAC with three core roles.

---

## Admin

**Purpose:** Platform oversight and governance authority.

### Capabilities
- assign roles
- change roles
- revoke access
- disable user access in the application lifecycle model
- view all records
- view full audit logs
- access dashboard

### Restrictions
- does not typically perform daily risk analysis
- not required to upload evidence during normal operations

Admin represents executive or security leadership.

---

## Analyst (Primary Active Role)

**Purpose:** Operational security governance.

This is the primary actor in the MVP.

### Capabilities
- create / update / delete assets
- create and score risks
- assign risk treatments
- map controls to assets or risks
- update control implementation status
- upload evidence metadata
- view dashboard
- participate in access reviews when required

### Restrictions
- cannot assign system roles
- cannot change application-level administrative configuration
- cannot approve their own privileged access changes

The Analyst drives the governance workflow.

---

## Auditor

**Purpose:** Independent verification.

### Capabilities
- read-only access to:
  - assets
  - risks
  - controls
  - evidence
  - dashboard
  - audit logs
  - access history views

### Restrictions
- cannot create, update, or delete records
- cannot modify risk scores
- cannot change control implementation status
- cannot assign roles
- cannot approve access changes

Auditor ensures compliance without altering system state.

---

# 4. Access Lifecycle Workflows (IAM Core)

These workflows are what make Governance Core feel like an IAM-aware system instead of only a secured CRUD application.

---

## Workflow A - Assign Role (Joiner)

### Trigger
A new user needs initial access to the platform.

### Actor
Admin

### Flow
1. admin selects an existing authenticated identity
2. admin assigns a role such as `ANALYST`
3. system records the access decision
4. user logs in
5. backend enforces access based on the assigned role

### IAM Meaning
This represents the **Joiner** part of JML.

### Audit Expectation
The system records:
- actor
- action (`ASSIGN_ROLE`)
- target user
- old value
- new value
- timestamp
- outcome

---

## Workflow B - Change Role (Mover)

### Trigger
A user's job function changes.

### Actor
Admin

### Flow
1. admin reviews current access
2. admin changes role from one state to another
   - example: `ANALYST` -> `AUDITOR`
3. system records the change
4. new access applies on subsequent authenticated requests

### IAM Meaning
This represents the **Mover** part of JML.

### Audit Expectation
The system records:
- actor
- action (`CHANGE_ROLE`)
- target user
- previous role
- new role
- timestamp
- outcome

---

## Workflow C - Revoke Role / Disable Access (Leaver)

### Trigger
A user leaves the organization or should no longer access the platform.

### Actor
Admin

### Flow
1. admin revokes the role or disables access
2. system records the event
3. user can no longer perform authorized actions
4. privileged access is removed as part of the lifecycle model

### IAM Meaning
This represents the **Leaver** part of JML.

### Audit Expectation
The system records:
- actor
- action (`REVOKE_ACCESS` or `DISABLE_USER`)
- target user
- previous access state
- new access state
- timestamp
- outcome

---

## Workflow D - Review Current Access

### Trigger
An admin or auditor needs to confirm who currently has what level of access.

### Actor
Admin or Auditor

### Flow
1. system displays current role assignments
2. admin or auditor reviews whether access is appropriate
3. any required change is performed through a controlled lifecycle workflow

### IAM Meaning
This starts moving the project toward **access review** and governance thinking.

---

# 5. Core Governance Workflows

These workflows remain the core GRC side of the platform.

---

## Step 1 - Login

User logs in via OIDC.

System:
- authenticates user through Keycloak
- determines role from the trusted backend-authenticated context
- enforces access policies via Spring Security and RBAC rules

IAM concepts:
- identity enforcement
- authorization boundaries
- least privilege

---

## Step 2 - Register Assets

### User Action (Analyst)
Creates asset with:
- name
- type
- owner
- data classification (`PUBLIC`, `INTERNAL`, `CONFIDENTIAL`, `RESTRICTED`)
- description

### Governance Outcome
- asset inventory established
- ownership defined
- data sensitivity modeled

This forms the governance baseline.

---

## Step 3 - Risk Management

### User Action
- create risk
- link to asset
- set likelihood
- set impact
- system auto-calculates score (`likelihood x impact`)
- assign treatment (`Accept / Mitigate / Transfer / Avoid`)

### Governance Outcome
- structured risk register
- quantified risk scoring
- traceable decision-making

Business logic demonstrated:
- derived fields
- domain validation
- risk exposure tracking

---

## Risk Assessment Methodology

Governance Core implements a **semi-quantitative risk assessment** approach aligned with ISO 27001 practices.

### Why Semi-Quantitative?

There are three common approaches to measuring risk:

| Approach | Description | Why Not Used for MVP |
|-----------|-------------|----------------------|
| Quantitative | Uses financial data and probabilities to calculate expected monetary loss | Requires reliable financial and statistical data that most organizations do not realistically possess |
| Qualitative | Uses descriptive categories (Low / Medium / High) without numeric structure | Too subjective and lacks prioritization granularity |
| **Semi-Quantitative** | Uses numeric scales to represent categories (e.g., 1-5) and calculates a derived score | Balanced, practical, and widely used in ISO 27001 implementations |

The MVP uses:

`Risk Score = Likelihood x Impact`

Where:
- likelihood: integer (`1-5`)
- impact: integer (`1-5`)
- risk score: derived integer (`1-25`)

### Risk Level Mapping

| Score Range | Risk Level |
|--------------|------------|
| 1-5 | LOW |
| 6-10 | MEDIUM |
| 11-15 | HIGH |
| 16-25 | CRITICAL |

This allows:
- risk prioritization
- executive-friendly reporting
- structured decision-making
- audit traceability

### Governance Rationale

Semi-quantitative assessment is commonly used in ISO 27001 risk matrices because:

- precise financial loss estimates are often unreliable
- overly precise numbers can create a false sense of certainty
- organizations need structured prioritization rather than actuarial modeling

This approach provides sufficient rigor for governance while remaining practical.

### Future Enhancement (Optional)

The architecture can later support:

- dynamic scoring adjustments based on asset classification
- quantitative financial modeling (e.g., Annual Loss Expectancy)
- custom risk calculation formulas per organization

For MVP scope, semi-quantitative provides the optimal balance between realism and implementation complexity.

---

## Step 4 - Control Mapping (Statement of Applicability)

### User Action
- view control catalog (ISO 27001 Annex A seeded list)
- mark control status:
  - implemented
  - planned
  - not applicable
- provide justification

### Governance Outcome
- control applicability modeled
- SoA logic captured
- compliance traceability established

---

## Step 5 - Evidence Attachment

### User Action
- upload evidence metadata
- link evidence to:
  - control
  - risk
  - asset
- set expiry date

### Governance Outcome
- audit-ready traceability
- control effectiveness proof
- continuous compliance logic (expiry forces renewal)

---

## Step 6 - Security Posture Dashboard

The dashboard aggregates system state.

### Displays
- total assets
- open risks
- high-risk count
- controls implemented percentage
- controls not applicable percentage
- expiring evidence
- access reviews due
- vulnerabilities (future AppSec module)

### Outcome

The user sees:
- risk exposure
- control coverage
- compliance readiness
- governance maturity

This fulfills the platform's core objective.

---

# 6. Access Control Enforcement Model

Authorization rules:

| Action | Admin | Analyst | Auditor |
|--------|--------|----------|----------|
| Assign Role | Yes | No | No |
| Change Role | Yes | No | No |
| Revoke / Disable Access | Yes | No | No |
| View Current Access State | Yes | Yes | Yes |
| Create Assets | Yes | Yes | No |
| Create Risks | Yes | Yes | No |
| Modify Controls | Yes | Yes | No |
| Upload Evidence | Yes | Yes | No |
| View Dashboard | Yes | Yes | Yes |
| View Audit Logs | Yes | Read-only | Read-only |

Enforcement implemented using:

- Spring Security
- backend RBAC rules
- JWT-based authentication
- frontend role-aware rendering
- audit logging

---

# 7. Security and Governance Principles Demonstrated

The platform is designed to reflect:

- least privilege
- separation of duties
- accountability
- traceability
- auditability
- continuous improvement
- identity-first architecture
- lifecycle-aware access governance

---

# 8. Architectural Scope (Intentional Constraints)

The current phase intentionally excludes:

- multi-tenancy
- advanced workflow engines
- full external provisioning connectors
- vendor management
- incident response automation
- AI governance logic
- external SIEM integrations

These may be added in future phases.

---

# 9. Learning Outcomes

By implementing this system, the developer practices:

IAM:
- OAuth2
- OIDC
- JWT validation
- RBAC
- access lifecycle modeling
- privileged access boundaries
- authorization enforcement
- access review thinking

GRC:
- risk modeling
- control traceability
- evidence lifecycle
- compliance alignment
- auditability

AppSec (future phase):
- vulnerability tracking
- remediation workflow
- risk-to-control mapping

---

# 10. Final Summary

Governance Core MVP is:

A single-tenant, multi-role, identity-enforced security governance engine with explicit access-governance direction.

It enables:

- structured asset inventory
- risk quantification
- control traceability
- evidence lifecycle management
- security posture visibility
- role-aware access governance
- lifecycle-oriented access changes
- auditable security workflows

All protected through robust IAM enforcement.
