# Compliance Framework Mapping

This document explains how Governance Core's design decisions reflect the requirements and principles of ISO 27001:2022, NIST Cybersecurity Framework, and PIPEDA. The platform does not claim certification — it is designed to help organizations pursue and maintain these frameworks.

---

## ISO 27001:2022

ISO 27001 is the international standard for Information Security Management Systems. It requires organizations to identify assets, assess risks, implement controls, and maintain evidence of compliance.

### How Governance Core maps to ISO 27001

| ISO 27001 Requirement | Governance Core Implementation |
|---|---|
| Asset inventory (Clause 8.1) | `AssetSubdomain` — every system, application, and data store is registered with owner, type, and classification |
| Risk assessment (Clause 6.1.2) | `RiskSubdomain` — risks are tied to assets, scored by likelihood × impact, and assigned a treatment status |
| Statement of Applicability / Annex A (Clause 6.1.3) | `ControlSubdomain` — ISO 27001:2022 Annex A controls are seeded as a catalog; `ControlImplementation` tracks applicability and status per asset |
| Evidence of conformity (Clause 7.5) | `EvidenceSubdomain` — evidence metadata is stored and linked to controls, risks, and assets with expiry tracking |
| Access control (Annex A 5.15–5.18) | `IAMSubdomain` — access assignments, privileged role flagging, and quarterly access reviews with evidence |
| Vulnerability management (Annex A 8.8) | `AppSecSubdomain` — vulnerabilities are tracked per asset and mapped to controls |
| Audit trail | Audit logging records changes to risk scores, control statuses, access decisions, and evidence links |

### Core traceability chain
```
Asset → Risk → Control → Implementation → Evidence
```
This chain directly satisfies ISO 27001's requirement that risks be treated, controls be applied, and evidence be retained for audit.

---

## NIST Cybersecurity Framework (CSF 2.0)

The NIST CSF organizes security activities into six functions: Govern, Identify, Protect, Detect, Respond, Recover.

### How Governance Core maps to NIST CSF

| NIST Function | Governance Core Implementation |
|---|---|
| **Govern** | RBAC (Admin, Analyst, Auditor roles) enforces accountability; audit logging tracks who changed what and when |
| **Identify** | `AssetSubdomain` builds the asset inventory — the foundation of any security program |
| **Identify** | `RiskSubdomain` performs risk assessment tied to each identified asset |
| **Protect** | `ControlSubdomain` tracks which controls are implemented per asset and why others are not applicable |
| **Protect** | `IAMSubdomain` enforces least-privilege through access assignments and periodic access reviews |
| **Detect** | `AppSecSubdomain` tracks vulnerabilities discovered through SAST, DAST, SCA, and penetration testing |
| **Respond** | Vulnerability remediation plans and corrective action tracking in `AppSecSubdomain` |
| **Recover** | Evidence of remediation actions stored and linked via `EvidenceSubdomain` |

### Design principle alignment
NIST CSF emphasizes continuous improvement. Governance Core supports this through evidence expiry logic — evidence does not remain valid indefinitely, requiring teams to renew proof of control effectiveness on a defined schedule.

---

## PIPEDA (Personal Information Protection and Electronic Documents Act)

PIPEDA is Canada's federal privacy law governing how private sector organizations collect, use, and disclose personal information. It is directly relevant to any organization operating in Canada.

### How Governance Core reflects PIPEDA principles

| PIPEDA Principle | Governance Core Implementation |
|---|---|
| **Accountability** | Every asset has a named owner. RBAC ensures only authorized roles can modify sensitive records. Audit logging creates an accountability trail. |
| **Identifying purposes** | `DataClassification` enum (PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED) requires teams to explicitly classify what data an asset holds and why. |
| **Limiting collection** | The platform stores evidence metadata and references only — not the underlying documents themselves — minimizing data exposure within the governance tool itself. |
| **Safeguards** | Access to the platform is controlled via OAuth2 (delegated to a trusted identity provider) and RBAC. No passwords are stored directly. |
| **Openness** | The `/docs` folder and audit logs provide transparency into how the system operates and who has access. |
| **Individual access** | Access review workflows in `IAMSubdomain` ensure that access to assets holding personal data is reviewed quarterly and revoked when no longer necessary. |
| **Challenging compliance** | Audit-ready reports provide evidence that access decisions, control implementations, and risk treatments were deliberate and documented. |

### PIPEDA-specific design decisions
- Assets classified as CONFIDENTIAL or RESTRICTED automatically carry higher risk exposure in the risk scoring model, prompting stronger control requirements.
- Access reviews are mandatory for assets holding personal data, with evidence of the review decision retained in `EvidenceSubdomain`.
- OAuth2 authentication means the platform never stores or manages user credentials, reducing the attack surface for a credential breach.

---

## Summary

| Framework | Primary subdomains | Key design decision |
|---|---|---|
| ISO 27001 | All — especially Control + Evidence | Full Asset → Risk → Control → Evidence traceability |
| NIST CSF | Asset, Risk, Control, AppSec, IAM | Subdomains map directly to Identify / Protect / Detect / Respond |
| PIPEDA | Asset, IAM, Evidence | DataClassification + access reviews + OAuth2 (no credential storage) |

---

*This document is maintained alongside the codebase and updated as new subdomains are implemented.*