# Governance Core — UI Architecture & Design Specification (Updated)

---

## 1. UI Philosophy

Governance Core is a security governance engine — not a generic CRUD application.

The UI must communicate:

- Authority
- Structural clarity
- Traceability
- Role boundaries
- Immediate security posture visibility

Primary objective (from operational blueprint):

> A user logs in and immediately understands the organization's security posture. :contentReference[oaicite:2]{index=2}

The interface must feel:

- Enterprise-grade
- Calm
- Structured
- Analytical
- Trustworthy

---

## 2. Core Workflow Alignment

The UI strictly mirrors the governance lifecycle:

Asset → Risk → Control → Evidence → Dashboard :contentReference[oaicite:3]{index=3}

This traceability chain also aligns directly with ISO 27001 expectations:

Asset → Risk → Control → Implementation → Evidence :contentReference[oaicite:4]{index=4}

Navigation order reflects this lifecycle.

---

## 3. Global Layout Structure

### Layout Composition

- Fixed Sidebar (left)
- Top Navigation Bar (persistent)
- Main Workspace Area

### Sidebar

Navigation order:

- Dashboard
- Assets
- Risks
- Controls
- Evidence
- Audit Logs
- Users (Admin only)

Sidebar is:

- Minimal
- Icon + label
- Active route highlighted
- No animation clutter

---

## 4. Persistent Security Posture Score

A numeric Security Posture Score (0–100) is displayed in the Top Bar.

Purpose:
- Provide instant health visibility
- Allow quick assessment without navigating to dashboard
- Reinforce governance maturity

The score may be derived from:

- Controls implemented %
- High/critical risk count
- Expiring evidence
- Open vulnerabilities (future phase)

The exact formula can evolve, but the UI placement remains constant.

---

## 5. Role-Aware UI Enforcement (IAM)

RBAC must be enforced visually — not only at route level.

Roles:

- Admin
- Analyst
- Auditor

Behavior:

Admin:
- Full access
- User management visible
- All action buttons enabled

Analyst:
- Create/update/delete domain records
- Cannot manage users

Auditor:
- Read-only access
- No create/update/delete buttons rendered
- Status selectors disabled

Buttons and actions are conditionally rendered.

This reflects:

- Least Privilege
- Separation of Duties
- Access Control enforcement :contentReference[oaicite:5]{index=5}

---

## 6. Design System

### Visual Tone

- Dark mode default
- High readability
- Structured spacing
- No decorative gradients

Subtle depth is allowed via:
- Light elevation
- Very subtle noise texture on cards
- Slight background contrast variations

No aggressive effects.

### Color Usage

Background: dark slate  
Cards: slightly lighter slate  
Primary accent: blue  
Success: green  
Warning: amber  
Critical: red  
Neutral: gray  

Red is reserved for CRITICAL states only.

---

## 7. Assets Page (Foundation Layer)

Purpose:
Establish asset inventory baseline.

ISO 27001 Clause 8.1 alignment :contentReference[oaicite:6]{index=6}

### Layout

Header:
- "Assets"
- "+ Add Asset" (hidden for Auditor)

Filters:
- Search
- Classification filter

Table Columns:
- Name
- Owner
- Classification
- Linked Risk Count
- Actions

### Add Asset Modal

Fields:
- Name
- Type
- Owner
- Data Classification
- Description

Empty State Copy:

"Assets form the foundation of your governance engine.  
Create your first asset to begin structured risk modeling."

---

## 8. Risk Page (Semi-Quantitative Modeling)

Implements semi-quantitative model:

Risk Score = Likelihood × Impact :contentReference[oaicite:7]{index=7}

Severity Mapping:

1–5: LOW  
6–10: MEDIUM  
11–15: HIGH  
16–25: CRITICAL  

### Layout

Header:
- "Risks"
- "+ Create Risk" (hidden for Auditor)

Table:
- Risk Title
- Linked Asset
- Likelihood
- Impact
- Score
- Severity Badge
- Treatment

Create/Edit:
- Sliders for Likelihood & Impact
- Live score calculation
- Real-time severity display

---

## 9. Controls Page (Statement of Applicability)

Purpose:
Model ISO Annex A control applicability :contentReference[oaicite:8]{index=8}

### Responsive Pattern

Desktop:
- Split view (catalog left, details right)

Tablet/Mobile:
- Control detail opens in right-side drawer
- Prevents cramped layout

### Control Detail Panel

- Control name & description
- Status selector:
  - Implemented
  - Planned
  - Not Applicable
- Justification field
- Linked assets
- Linked risks
- Linked evidence

---

## 10. Evidence Page (Continuous Compliance)

Purpose:
Maintain audit-ready proof.

ISO Clause 7.5 alignment :contentReference[oaicite:9]{index=9}

Layout:
- Card or table format

Fields:
- Evidence name
- Linked control
- Linked asset/risk
- Expiry date
- Status badge

Expiry Logic:
- <30 days: Warning
- Expired: Critical

Emphasizes continuous compliance lifecycle.

---

## 11. Dashboard (Security Posture View)

The Dashboard is the visual summary of governance maturity.

### Hero Section — Risk Matrix

5x5 interactive matrix:

Impact (vertical)  
Likelihood (horizontal)

Cells:
- Colored by severity
- Clickable to reveal underlying risks

The matrix is the most actionable governance visualization and must be visually dominant.

---

### Secondary Section — KPI Cards

- Total Assets
- Open Risks
- High/Critical Risks
- Controls Implemented %
- Expiring Evidence

---

### Compliance Snapshot

- Implemented %
- Planned %
- Not Applicable %
- Evidence expiring soon

Provides ISO/NIST posture alignment :contentReference[oaicite:10]{index=10}

---

## 12. Users Page (Admin Only)

Purpose:
Role management and governance authority.

Columns:
- Name
- Email
- Role
- Actions

Role dropdown:
- Admin
- Analyst
- Auditor

Reinforces separation of duties.

---

## 13. UX Principles

- No unnecessary page reloads
- Drawer pattern for details
- Clear loading states
- Clear error states
- Clear empty states
- Minimal animation
- Structured transitions

The UI must feel calm and defensible — not flashy.

---

## 14. Architectural Symmetry

Frontend mirrors backend subdomains:

- AssetSubdomain → Assets UI
- RiskSubdomain → Risks UI
- ControlSubdomain → Controls UI
- EvidenceSubdomain → Evidence UI
- IAMSubdomain → Role-aware UI enforcement

This preserves domain integrity across layers.

---

## 15. Final Objective

When any role accesses the platform, they must immediately understand:

- What assets exist
- What risks are active
- What controls are implemented
- What evidence is expiring
- What the overall posture score indicates

The UI exists to make governance visible, structured, and auditable.