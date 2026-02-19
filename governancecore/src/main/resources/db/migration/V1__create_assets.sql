CREATE TABLE assets (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    asset_id VARCHAR(36) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    owner VARCHAR(255) NOT NULL,
    asset_type VARCHAR(50) NOT NULL,
    classification VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
