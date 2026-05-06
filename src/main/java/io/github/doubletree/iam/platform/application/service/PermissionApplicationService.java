package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionApplicationService {

    private final PermissionRepository permissionRepository;

    public PermissionApplicationService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public Permission createPermission(String name) {
        return permissionRepository.save(Permission.create(name));
    }
}
