package io.github.doubletree.iam.platform.web;

import io.github.doubletree.iam.platform.application.service.PermissionApplicationService;
import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.web.dto.CreatePermissionRequest;
import io.github.doubletree.iam.platform.web.dto.PermissionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionApplicationService permissionApplicationService;

    public PermissionController(PermissionApplicationService permissionApplicationService) {
        this.permissionApplicationService = permissionApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermissionResponse createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        Permission permission = permissionApplicationService.createPermission(request.name());
        return PermissionResponse.from(permission);
    }
}
