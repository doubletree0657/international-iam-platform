ALTER TABLE roles
    ADD COLUMN tenant_id UUID;

UPDATE roles
SET tenant_id = (
    SELECT id
    FROM tenants
    ORDER BY created_at, id
    LIMIT 1
)
WHERE tenant_id IS NULL;

ALTER TABLE roles
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE roles
    ADD CONSTRAINT fk_roles_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id);

ALTER TABLE roles
    DROP CONSTRAINT uq_roles_name;

ALTER TABLE roles
    ADD CONSTRAINT uq_roles_tenant_name UNIQUE (tenant_id, name);
