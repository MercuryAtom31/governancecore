# Governance Core — User Workflows & Role Model

This document defines:

- What the primary user does in the MVP
- The role model (Admin / Analyst / Auditor)
- How IAM, GRC, and AppSec concepts are enforced
- What security posture visibility means in practice

This is the operational blueprint of the platform.

---

# 1. MVP Objective

## Core Goal

> A user logs in and immediately understands the organization's security posture.

Security posture is derived from structured workflows:

Asset → Risk → Control → Evidence → Dashboard

The platform centralizes these elements into a traceable governance engine.

---

# 2. Authentication & Identity (IAM Layer)

## Login Flow

Users authenticate using OAuth2 (Google / GitHub / Corporate IdP).

The platform:
- Does NOT store passwords
- Delegates authentication to a trusted identity provider
- Issues a session (JWT-based)
- Resolves the user’s assigned role

## Identity Concepts Practiced

- OAuth2 / OIDC
- Token validation
- Role-based access control (RBAC)
- Privileged role enforcement
- Audit logging of identity events

---

# 3. Role Model

The platform implements strict RBAC with three core roles.

---

## 👑 Admin

**Purpose:** Platform oversight and governance authority.

### Capabilities:
- Manage users
- Assign / change roles
- View all records
- Override risk or control statuses (if required)
- View full audit logs
- Access dashboard

### Restrictions:
- Does not typically perform daily risk analysis
- Not required to upload evidence

Admin represents executive or security leadership.

---

## 🧠 Analyst (Primary Active Role)

**Purpose:** Operational security governance.

This is the primary actor in the MVP.

### Capabilities:
- Create / update / delete assets
- Create and score risks
- Assign risk treatments
- Map controls to assets or risks
- Update control implementation status
- Upload evidence metadata
- View dashboard
- Participate in access reviews

### Restrictions:
- Cannot manage user roles
- Cannot modify system configuration

The Analyst drives the governance workflow.

---

## 👀 Auditor

**Purpose:** Independent verification.

### Capabilities:
- Read-only access to:
  - Assets
  - Risks
  - Controls
  - Evidence
  - Dashboard
  - Audit logs

### Restrictions:
- Cannot create, update, or delete records
- Cannot modify risk scores
- Cannot change control implementation status
- Cannot assign roles

Auditor ensures compliance without altering system state.

---

# 4. Core MVP Workflows

---

## 🧱 Step 1 – Login

User logs in via OAuth2.

System:
- Authenticates user
- Determines role
- Enforces access policies via `@PreAuthorize`

IAM concepts:
- Identity enforcement
- Authorization boundaries
- Least privilege

---

## 🧱 Step 2 – Register Assets

### User Action (Analyst):
Creates asset with:
- Name
- Type
- Owner
- Data classification (PUBLIC / INTERNAL / CONFIDENTIAL / RESTRICTED)
- Description

### Governance Outcome:
- Asset inventory established
- Ownership defined
- Data sensitivity modeled

This forms the governance baseline.

---

## 🧱 Step 3 – Risk Management

### User Action:
- Create risk
- Link to asset
- Set likelihood
- Set impact
- System auto-calculates score (likelihood × impact)
- Assign treatment (Accept / Mitigate / Transfer / Avoid)

### Governance Outcome:
- Structured risk register
- Quantified risk scoring
- Traceable decision-making

Business logic demonstrated:
- Derived fields
- Domain validation
- Risk exposure tracking

---

### 🔎 Risk Assessment Methodology

Governance Core implements a **Semi-Quantitative Risk Assessment approach** aligned with ISO 27001 practices.

### Why Semi-Quantitative?

There are three common approaches to measuring risk:

| Approach | Description | Why Not Used for MVP |
|-----------|-------------|----------------------|
| Quantitative | Uses financial data and probabilities to calculate expected monetary loss | Requires reliable financial and statistical data that most organizations do not realistically possess |
| Qualitative | Uses descriptive categories (Low / Medium / High) without numeric structure | Too subjective and lacks prioritization granularity |
| **Semi-Quantitative** | Uses numeric scales to represent categories (e.g., 1–5) and calculates a derived score | Balanced, practical, and widely used in ISO 27001 implementations |

The MVP uses:

Risk Score = Likelihood × Impact

Where:

- Likelihood: integer (1–5)
- Impact: integer (1–5)
- Risk Score: derived integer (1–25)

### Risk Level Mapping

Risk scores are mapped to severity levels:

| Score Range | Risk Level |
|--------------|------------|
| 1–5 | LOW |
| 6–10 | MEDIUM |
| 11–15 | HIGH |
| 16–25 | CRITICAL |

This allows:

- Risk prioritization
- Executive-friendly reporting
- Structured decision-making
- Audit traceability

### Governance Rationale

Semi-quantitative assessment is commonly used in ISO 27001 risk matrices because:

- Precise financial loss estimates are often unreliable
- Overly precise numbers can create a false sense of certainty
- Organizations need structured prioritization rather than actuarial modeling

This approach provides sufficient rigor for governance while remaining practical.

### Future Enhancement (Optional)

The architecture can later support:

- Dynamic scoring adjustments based on asset classification
- Quantitative financial modeling (e.g., Annual Loss Expectancy)
- Custom risk calculation formulas per organization

For MVP scope, semi-quantitative provides the optimal balance between realism and implementation complexity.
---

## 🧱 Step 4 – Control Mapping (Statement of Applicability)

### User Action:
- View control catalog (ISO 27001 Annex A seeded list)
- Mark control status:
  - Implemented
  - Planned
  - Not Applicable
- Provide justification

### Governance Outcome:
- Control applicability modeled
- SoA logic captured
- Compliance traceability established

---

## 🧱 Step 5 – Evidence Attachment

### User Action:
- Upload evidence metadata
- Link evidence to:
  - Control
  - Risk
  - Asset
- Set expiry date

### Governance Outcome:
- Audit-ready traceability
- Control effectiveness proof
- Continuous compliance logic (expiry forces renewal)

---

## 🧱 Step 6 – Security Posture Dashboard

The dashboard aggregates system state.

### Displays:

- Total assets
- Open risks
- High-risk count
- Controls implemented %
- Controls not applicable %
- Expiring evidence
- Access reviews due
- Vulnerabilities (future AppSec module)

### Outcome:

The user sees:
- Risk exposure
- Control coverage
- Compliance readiness
- Governance maturity

This fulfills the platform’s core objective.

---

# 5. Access Control Enforcement Model

Authorization rules:

| Action | Admin | Analyst | Auditor |
|--------|--------|----------|----------|
| Manage Users | ✅ | ❌ | ❌ |
| Create Assets | ✅ | ✅ | ❌ |
| Create Risks | ✅ | ✅ | ❌ |
| Modify Controls | ✅ | ✅ | ❌ |
| Upload Evidence | ✅ | ✅ | ❌ |
| View Dashboard | ✅ | ✅ | ✅ |
| View Audit Logs | ✅ | Read-only | Read-only |

Enforcement implemented using:

- Spring Security
- `@PreAuthorize`
- JWT-based authentication
- Audit logging

---

# 6. Security Design Principles Demonstrated

The MVP reflects:

- Least Privilege
- Separation of Duties
- Accountability
- Traceability
- Continuous Improvement
- Identity-first architecture

---

# 7. Architectural Scope (Intentional Constraints)

The MVP intentionally excludes:

- Multi-tenancy
- Advanced workflow engines
- Vendor management
- Incident response automation
- AI governance logic
- External SIEM integrations

These may be added in future phases.

---

# 8. Learning Outcomes

By implementing this system, the developer practices:

IAM:
- OAuth2
- JWT
- RBAC
- Privileged access
- Authorization enforcement

GRC:
- Risk modeling
- Control traceability
- Evidence lifecycle
- Compliance alignment

AppSec (future phase):
- Vulnerability tracking
- Remediation workflow
- Risk-to-control mapping

---

# 9. Final Summary

Governance Core MVP is:

A single-tenant, multi-role, identity-enforced security governance engine.

It enables:

- Structured asset inventory
- Risk quantification
- Control traceability
- Evidence lifecycle management
- Security posture visibility

All protected through robust IAM enforcement.