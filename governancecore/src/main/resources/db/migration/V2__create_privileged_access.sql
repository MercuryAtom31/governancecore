CREATE TABLE privileged_access (
    id UUID PRIMARY KEY,
    privileged_access_id VARCHAR(36) NOT NULL UNIQUE,
    requester_user_id VARCHAR(255) NOT NULL,
    requester_first_name VARCHAR(255) NOT NULL,
    requester_last_name VARCHAR(255) NOT NULL,
    requester_current_role VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    duration_minutes VARCHAR(20) NOT NULL,
    justification TEXT NOT NULL,
    requested_at TIMESTAMPTZ NOT NULL,
    status VARCHAR(30) NOT NULL,
    granted_by_user_id VARCHAR(255),
    granted_by_first_name VARCHAR(255),
    granted_by_last_name VARCHAR(255),
    approval_note TEXT,
    granted_at TIMESTAMPTZ,
    refused_at TIMESTAMPTZ,
    refusal_reason TEXT,
    expires_at TIMESTAMPTZ,
    revoked_at TIMESTAMPTZ,
    revoke_reason TEXT,
    CONSTRAINT chk_privileged_access_role_admin
        CHECK (role = 'ADMIN'),
    CONSTRAINT chk_privileged_access_duration
        CHECK (duration_minutes IN ('MINUTES_15', 'MINUTES_30', 'MINUTES_60')),
    CONSTRAINT chk_privileged_access_status
        CHECK (status IN ('REQUESTED', 'GRANTED', 'IN_USE', 'REFUSED', 'EXPIRED', 'REVOKED'))
);

-- Indexes tuned for common privileged access workflows and lifecycle checks.
CREATE INDEX idx_privileged_access_requester_user_id ON privileged_access(requester_user_id);
CREATE INDEX idx_privileged_access_status ON privileged_access(status);
CREATE INDEX idx_privileged_access_expires_at ON privileged_access(expires_at);
CREATE INDEX idx_privileged_access_requested_at ON privileged_access(requested_at);

-- How to create a table from an entity class?
-- When you look at an entity class, read it like this:
-- What is the table name?
-- What is the primary key?
-- What fields need columns?
-- What are the column names?
-- Which columns cannot be null?
-- Which columns must be unique?
-- What SQL data type matches each Java type?
-- Are there embedded objects that become columns too?
-- Are there timestamps or audit fields?
-- Which columns should be indexed?

-- Used TIMESTAMPTZ instead of TIMESTAMP to ensure all timestamps are stored with time zone awareness (UTC).
-- This is critical for security, audit, and IAM workflows where events may occur across different systems and regions.
-- TIMESTAMPTZ guarantees consistent time comparison, prevents ambiguity, and ensures accurate ordering of events
-- (e.g., access granted, expired, refused, or revoked), which is essential for audit logs and forensic analysis.