package io.github.doubletree.iam.platform.web.dto;

import io.github.doubletree.iam.platform.domain.Permission;
import java.util.UUID;

public record PermissionResponse(UUID id, String name) {

    public static PermissionResponse from(Permission permission) {
        return new PermissionResponse(permission.getId(), permission.getName());
    }
}
