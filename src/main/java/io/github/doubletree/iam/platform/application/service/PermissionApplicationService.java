package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionApplicationService {

    private final PermissionRepository permissionRepository;
    private final AuditApplicationService auditApplicationService;

    public PermissionApplicationService(
            PermissionRepository permissionRepository,
            AuditApplicationService auditApplicationService) {
        this.permissionRepository = permissionRepository;
        this.auditApplicationService = auditApplicationService;
    }

    @Transactional
    public Permission createPermission(String name) {
        Permission permission = permissionRepository.save(Permission.create(name));
        auditApplicationService.recordEvent(null, "PERMISSION_CREATED", "PERMISSION", permission.getId());
        return permission;
    }
}
