package io.github.doubletree.iam.platform.web;

import io.github.doubletree.iam.platform.application.service.PermissionApplicationService;
import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.web.dto.CreatePermissionRequest;
import io.github.doubletree.iam.platform.web.dto.PermissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "Permissions", description = "Permission management APIs")
@SecurityRequirement(name = OpenApiConfiguration.BEARER_AUTH)
public class PermissionController {

    private final PermissionApplicationService permissionApplicationService;

    public PermissionController(PermissionApplicationService permissionApplicationService) {
        this.permissionApplicationService = permissionApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create permission", description = "Requires iam.write scope.")
    public PermissionResponse createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        Permission permission = permissionApplicationService.createPermission(request.name());
        return PermissionResponse.from(permission);
    }
}
