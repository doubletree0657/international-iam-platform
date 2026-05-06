package io.github.doubletree.iam.platform.web.dto;

import io.github.doubletree.iam.platform.domain.Tenant;
import java.util.UUID;

public record TenantResponse(UUID id, String name) {

    public static TenantResponse from(Tenant tenant) {
        return new TenantResponse(tenant.getId(), tenant.getName());
    }
}
