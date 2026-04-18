CREATE TABLE privileged_access (
    id UUID PRIMARY KEY,
    privileged_access_id VARCHAR(36) NOT NULL UNIQUE,
    target_user_id VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    approved_by VARCHAR(255) NOT NULL,
    granted_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP
);

CREATE INDEX idx_privileged_access_target_user_id ON privileged_access(target_user_id);
CREATE INDEX idx_privileged_access_expires_at ON privileged_access(expires_at);
