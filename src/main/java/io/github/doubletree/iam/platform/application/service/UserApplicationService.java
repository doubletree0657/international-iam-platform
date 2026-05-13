package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.application.exception.EntityNotFoundException;
import io.github.doubletree.iam.platform.application.exception.PasswordValidationException;
import io.github.doubletree.iam.platform.application.exception.TenantBoundaryViolationException;
import io.github.doubletree.iam.platform.domain.AccountStatus;
import io.github.doubletree.iam.platform.domain.Role;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.domain.User;
import io.github.doubletree.iam.platform.repository.RoleRepository;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import io.github.doubletree.iam.platform.repository.UserRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserApplicationService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final AuditApplicationService auditApplicationService;
    private final PasswordEncoder passwordEncoder;

    public UserApplicationService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            RoleRepository roleRepository,
            AuditApplicationService auditApplicationService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
        this.auditApplicationService = auditApplicationService;
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    public User setInitialPassword(UUID userId, String rawPassword) {
        User user = loadUser(userId);
        applyPasswordChange(user, rawPassword);
        user.setPasswordResetRequired(false);
        user.setAccountStatus(AccountStatus.ACTIVE);
        User savedUser = userRepository.save(user);
        auditApplicationService.recordEvent(
                savedUser.getTenant().getId(), "USER_PASSWORD_SET", "USER", savedUser.getId());
        return savedUser;
    }

    @Transactional
    public User updatePassword(UUID userId, String rawPassword) {
        User user = loadUser(userId);
        applyPasswordChange(user, rawPassword);
        user.setPasswordResetRequired(false);
        User savedUser = userRepository.save(user);
        auditApplicationService.recordEvent(
                savedUser.getTenant().getId(), "USER_PASSWORD_UPDATED", "USER", savedUser.getId());
        return savedUser;
    }

    @Transactional
    public User requirePasswordReset(UUID userId) {
        User user = loadUser(userId);
        user.setPasswordResetRequired(true);
        User savedUser = userRepository.save(user);
        auditApplicationService.recordEvent(
                savedUser.getTenant().getId(), "USER_PASSWORD_RESET_REQUIRED", "USER", savedUser.getId());
        return savedUser;
    }

    @Transactional
    public User clearPasswordResetRequired(UUID userId) {
        User user = loadUser(userId);
        user.setPasswordResetRequired(false);
        User savedUser = userRepository.save(user);
        auditApplicationService.recordEvent(
                savedUser.getTenant().getId(), "USER_PASSWORD_RESET_CLEARED", "USER", savedUser.getId());
        return savedUser;
    }

    private User loadUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private void applyPasswordChange(User user, String rawPassword) {
        validatePassword(rawPassword);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setPasswordUpdatedAt(Instant.now());
        user.setCredentialsVersion(user.getCredentialsVersion() + 1);
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null) {
            throw new PasswordValidationException("Password must be provided");
        }
        if (rawPassword.isBlank()) {
            throw new PasswordValidationException("Password must not be blank");
        }
        if (rawPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new PasswordValidationException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
    }
}
