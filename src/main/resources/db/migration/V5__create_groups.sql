CREATE TABLE groups (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_groups_tenant_name UNIQUE (tenant_id, name),
    CONSTRAINT fk_groups_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id)
);

CREATE TABLE group_users (
    group_id UUID NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (group_id, user_id),
    CONSTRAINT fk_group_users_group FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT fk_group_users_user FOREIGN KEY (user_id) REFERENCES users (id)
);
