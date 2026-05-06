package io.github.doubletree.iam.platform.web;

import io.github.doubletree.iam.platform.application.service.RoleApplicationService;
import io.github.doubletree.iam.platform.domain.Role;
import io.github.doubletree.iam.platform.web.dto.CreateRoleRequest;
import io.github.doubletree.iam.platform.web.dto.RoleResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleApplicationService roleApplicationService;

    public RoleController(RoleApplicationService roleApplicationService) {
        this.roleApplicationService = roleApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@Valid @RequestBody CreateRoleRequest request) {
        Role role = roleApplicationService.createRole(request.tenantId(), request.name());
        return RoleResponse.from(role);
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public RoleResponse assignPermissionToRole(@PathVariable UUID roleId, @PathVariable UUID permissionId) {
        Role role = roleApplicationService.assignPermissionToRole(roleId, permissionId);
        return RoleResponse.from(role);
    }
}
