ALTER TABLE users
    ADD COLUMN password_hash VARCHAR(255),
    ADD COLUMN password_updated_at TIMESTAMPTZ,
    ADD COLUMN password_reset_required BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN credentials_version INTEGER NOT NULL DEFAULT 1,
    ADD COLUMN account_status VARCHAR(32) NOT NULL DEFAULT 'PENDING';
