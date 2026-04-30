CREATE TABLE assets (
    id UUID PRIMARY KEY,
    asset_id VARCHAR(36) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    owner VARCHAR(255) NOT NULL,
    asset_type VARCHAR(50) NOT NULL,
    classification VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
