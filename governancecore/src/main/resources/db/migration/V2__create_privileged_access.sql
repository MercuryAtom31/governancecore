CREATE TABLE privileged_access (
    id UUID PRIMARY KEY,
    privileged_access_id VARCHAR(36) NOT NULL UNIQUE,
    target_user_id VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    approved_by VARCHAR(255) NOT NULL,
    granted_at TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ
);

-- The following indexes are created to optimize queries on target_user_id and expires_at columns
-- These indexes will improve the performance of queries that filter by target_user_id and expires_at, which are common in privileged access management scenarios.
CREATE INDEX idx_privileged_access_target_user_id ON privileged_access(target_user_id);
CREATE INDEX idx_privileged_access_expires_at ON privileged_access(expires_at);


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
-- (e.g., access granted, expired, or revoked), which is essential for audit logs and forensic analysis.