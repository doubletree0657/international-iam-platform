package io.github.doubletree.iam.platform.web.dto;

import io.github.doubletree.iam.platform.domain.Role;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record RoleResponse(UUID id, UUID tenantId, String name, Set<UUID> permissionIds) {

    public static RoleResponse from(Role role) {
        Set<UUID> permissionIds = role.getPermissions().stream()
                .map(permission -> permission.getId())
                .collect(Collectors.toSet());
        return new RoleResponse(role.getId(), role.getTenant().getId(), role.getName(), permissionIds);
    }
}
