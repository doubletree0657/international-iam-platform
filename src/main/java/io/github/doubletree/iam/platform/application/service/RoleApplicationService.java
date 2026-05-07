package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.domain.Role;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.repository.PermissionRepository;
import io.github.doubletree.iam.platform.repository.RoleRepository;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleApplicationService {

    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final PermissionRepository permissionRepository;
    private final AuditApplicationService auditApplicationService;

    public RoleApplicationService(
            RoleRepository roleRepository,
            TenantRepository tenantRepository,
            PermissionRepository permissionRepository,
            AuditApplicationService auditApplicationService) {
        this.roleRepository = roleRepository;
        this.tenantRepository = tenantRepository;
        this.permissionRepository = permissionRepository;
        this.auditApplicationService = auditApplicationService;
    }

    @Transactional
    public Role createRole(UUID tenantId, String name) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found: " + tenantId));

        Role role = roleRepository.save(Role.create(tenant, name));
        auditApplicationService.recordEvent(tenant.getId(), "ROLE_CREATED", "ROLE", role.getId());
        return role;
    }

    @Transactional
    public Role assignPermissionToRole(UUID roleId, UUID permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found: " + permissionId));

        role.getPermissions().add(permission);
        Role savedRole = roleRepository.save(role);
        auditApplicationService.recordEvent(
                savedRole.getTenant().getId(), "PERMISSION_ASSIGNED_TO_ROLE", "ROLE", savedRole.getId());
        return savedRole;
    }
}
