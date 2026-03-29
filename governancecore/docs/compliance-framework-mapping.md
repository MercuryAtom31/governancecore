# Compliance Framework Mapping

This document explains how Governance Core's design decisions and planned workflows align with ISO 27001:2022, NIST Cybersecurity Framework (CSF 2.0), and PIPEDA.

The platform does **not** claim certification.
It is designed to help demonstrate and practice security governance concepts that support those frameworks.

Important distinction:
- some mappings correspond to features that are already implemented
- some mappings correspond to features that are planned for the next phases

That distinction matters because compliance alignment is stronger when tied to real system behavior rather than only intention.

---

## 1. Current Security and IAM Features Already Implemented

The current phase already includes:

- OIDC login through Keycloak
- JWT validation in the Spring Boot backend
- role-based access control (`ADMIN`, `ANALYST`, `AUDITOR`)
- backend enforcement of read/write access rules
- frontend role-aware rendering for the Asset feature
- a trusted `/api/v1/auth/me` endpoint
- a documented security architecture with explicit IdP and application boundary

These implemented features already align with parts of ISO 27001, NIST CSF, and PIPEDA, especially around identity and access control.

---

## 2. Planned IAM Governance Features

The next phases are expected to add:

- audit logging of important governance and access actions
- access lifecycle workflows
  - assign role
  - change role
  - revoke role
  - disable access
- provisioning / deprovisioning simulation
- richer admin and audit views built on top of those backend workflows

These planned features strengthen the compliance mapping significantly because they move the system from authentication and RBAC into governance, accountability, and lifecycle management.

---

## 3. ISO 27001:2022

ISO 27001 is the international standard for Information Security Management Systems.
It expects organizations to identify assets, assess risks, implement controls, and maintain evidence of security decisions and activities.

### How Governance Core maps to ISO 27001

| ISO 27001 Area | Governance Core Alignment | Status |
|---|---|---|
| Asset inventory | Assets are registered with owner, type, classification, and description | Planned / partially implemented |
| Risk assessment | Risks are intended to be linked to assets and scored with a defined methodology | Planned |
| Statement of Applicability / control applicability | Control mapping and implementation status are part of the intended governance workflow | Planned |
| Evidence of conformity | Evidence tracking is part of the intended governance model | Planned |
| Access control | OIDC login, JWT validation, RBAC, role-aware UI, and protected endpoints | Implemented |
| Accountability / audit trail | Audit logging of access and governance actions is planned as a first-class backend concern | Planned |
| Lifecycle-sensitive access governance | role assignment, change, revocation, and disable flows are planned | Planned |

### ISO-relevant IAM alignment

The current IAM/security implementation already supports important ISO 27001 access-control ideas:

- delegated authentication through a trusted identity provider
- least privilege through role-based access rules
- separation between administrative and operational roles
- auditor read-only model
- backend-first access enforcement

### Why this matters

From an ISO point of view, it is not enough to say that roles exist.
The system must show that access is controlled, limited by role, and eventually auditable.

Governance Core is moving in that direction by building:
- authentication first
- authorization second
- lifecycle and audit third

---

## 4. NIST Cybersecurity Framework (CSF 2.0)

The NIST CSF organizes security outcomes into functions such as Govern, Identify, Protect, Detect, Respond, and Recover.

### How Governance Core maps to NIST CSF

| NIST Function | Governance Core Alignment | Status |
|---|---|---|
| Govern | documented role model, design decisions, planned auditability, planned access lifecycle workflows | Partially implemented |
| Identify | asset inventory and risk modeling direction | Planned / partially implemented |
| Protect | OIDC login, JWT validation, RBAC, route protection, frontend role-aware rendering | Implemented |
| Detect | future AppSec / vulnerability workflows and audit review visibility | Planned |
| Respond | future remediation and governance response workflows | Planned |
| Recover | future evidence and recovery-oriented traceability | Planned |

### NIST-relevant IAM alignment

The strongest current NIST alignment is in the **Protect** function:

- identity is established through Keycloak
- the backend validates trusted tokens
- roles determine what actions are allowed
- write operations are restricted by role
- frontend behavior reflects those same rules for usability

The strongest next alignment will come from **Govern** once audit logging and lifecycle workflows are added.

### Plain English explanation

NIST CSF wants organizations to know:
- what they have
- who can access it
- how they protect it
- how they track important security actions

The current Governance Core work already addresses the access-control side of that model.

---

## 5. PIPEDA (Personal Information Protection and Electronic Documents Act)

PIPEDA is Canada's federal privacy law for the private sector.
It is especially relevant when systems store, classify, or govern access to information that may involve personal data.

### How Governance Core reflects PIPEDA principles

| PIPEDA Principle | Governance Core Alignment | Status |
|---|---|---|
| Accountability | role boundaries and planned audit logging support accountability | Partially implemented |
| Safeguards | OIDC login, no local password storage, JWT validation, backend RBAC | Implemented |
| Limiting exposure | delegated authentication reduces credential handling inside the app | Implemented |
| Openness | documentation and explicit design decisions explain how access works | Implemented |
| Individual access / review sensitivity | planned access review and lifecycle workflows strengthen controlled access | Planned |
| Challenging compliance | future audit logs and traceable governance workflows support reviewability | Planned |

### PIPEDA-relevant design points

Governance Core already reflects a few privacy-respecting architectural choices:

- the application does not manage user passwords itself
- authentication is delegated to Keycloak
- access is limited by role
- the system is moving toward auditable access changes rather than silent privilege drift

### Plain English explanation

For privacy and compliance, it is not enough to store data carefully.
You also need to control who can access it and be able to explain those access decisions.

That is exactly why lifecycle workflows and audit logging matter so much in the next phase.

---

## 6. IAM-Specific Compliance Strengthening

The following IAM features are especially valuable because they strengthen all three frameworks at once.

| IAM Feature | Why It Matters | Status |
|---|---|---|
| OIDC login through Keycloak | trusted authentication boundary | Implemented |
| JWT validation in backend | trusted identity verification for API calls | Implemented |
| RBAC | least privilege and role separation | Implemented |
| Frontend role-aware rendering | better usability aligned with backend rules | Implemented |
| `/api/v1/auth/me` | trusted identity bridge from backend to frontend | Implemented |
| Audit logging | accountability and traceability | Planned |
| Assign / change / revoke / disable access | lifecycle governance and JML alignment | Planned |
| Provisioning / deprovisioning simulation | demonstrates managed access change over time | Planned |

---

## 7. Summary

| Framework | Strongest current alignment | Strongest next-phase alignment |
|---|---|---|
| ISO 27001 | access control, least privilege, trusted authentication boundary | audit logging, lifecycle controls, evidence-backed governance |
| NIST CSF | Protect function through identity and access control | Govern function through lifecycle and audit workflows |
| PIPEDA | safeguards, credential handling reduction, controlled access | accountability, access review, auditability |

---

## 8. Final Note

Governance Core is strongest when its compliance mapping is tied to real implemented behavior.

That is why the current roadmap emphasizes this order:

1. authentication and authorization foundation
2. role-aware frontend and backend enforcement
3. audit logging
4. access lifecycle workflows
5. provisioning / deprovisioning simulation
6. richer administrative and audit views

This keeps the compliance story technically defensible instead of overly aspirational.

---

*This document is maintained alongside the codebase and should be updated as implemented features replace planned ones.*
