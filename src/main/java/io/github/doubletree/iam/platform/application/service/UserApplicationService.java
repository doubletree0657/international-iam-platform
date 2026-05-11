package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.application.exception.EntityNotFoundException;
import io.github.doubletree.iam.platform.application.exception.TenantBoundaryViolationException;
import io.github.doubletree.iam.platform.domain.Role;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.domain.User;
import io.github.doubletree.iam.platform.repository.RoleRepository;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import io.github.doubletree.iam.platform.repository.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final AuditApplicationService auditApplicationService;

    public UserApplicationService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            RoleRepository roleRepository,
            AuditApplicationService auditApplicationService) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
        this.auditApplicationService = auditApplicationService;
    }

    @Transactional
    public User createUser(UUID tenantId, String username, String displayName) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found: " + tenantId));

        User user = userRepository.save(User.create(tenant, username, displayName));
        auditApplicationService.recordEvent(tenant.getId(), "USER_CREATED", "USER", user.getId());
        return user;
    }

    @Transactional(readOnly = true)
    public User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    @Transactional
    public User assignRoleToUser(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));

        if (!user.getTenant().getId().equals(role.getTenant().getId())) {
            throw new TenantBoundaryViolationException("User and role must belong to the same tenant");
        }

        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        auditApplicationService.recordEvent(
                savedUser.getTenant().getId(), "ROLE_ASSIGNED_TO_USER", "USER", savedUser.getId());
        return savedUser;
    }
}
