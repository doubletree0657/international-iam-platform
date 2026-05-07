CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID,
    actor VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    resource_type VARCHAR(255) NOT NULL,
    resource_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_tenant_id_created_at ON audit_logs (tenant_id, created_at);
CREATE INDEX idx_audit_logs_resource ON audit_logs (resource_type, resource_id);
