package io.github.doubletree.iam.platform.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.doubletree.iam.platform.application.exception.PasswordValidationException;
import io.github.doubletree.iam.platform.application.exception.TenantBoundaryViolationException;
import io.github.doubletree.iam.platform.application.result.MfaEnrollmentResult;
import io.github.doubletree.iam.platform.domain.AccountStatus;
import io.github.doubletree.iam.platform.domain.AuditLog;
import io.github.doubletree.iam.platform.domain.Client;
import io.github.doubletree.iam.platform.domain.Group;
import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.domain.Role;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.domain.User;
import io.github.doubletree.iam.platform.repository.AuditLogRepository;
import io.github.doubletree.iam.platform.repository.ClientRepository;
import io.github.doubletree.iam.platform.repository.GroupRepository;
import io.github.doubletree.iam.platform.repository.PermissionRepository;
import io.github.doubletree.iam.platform.repository.RoleRepository;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import io.github.doubletree.iam.platform.repository.UserRepository;
import io.github.doubletree.iam.platform.security.PasswordEncodingConfiguration;
import io.github.doubletree.iam.platform.security.crypto.SecretEncryptionService;
import io.github.doubletree.iam.platform.web.dto.UserResponse;
import java.lang.reflect.RecordComponent;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        TenantApplicationService.class,
        UserApplicationService.class,
        RoleApplicationService.class,
        PermissionApplicationService.class,
        ClientApplicationService.class,
        GroupApplicationService.class,
        AuditApplicationService.class,
        PasswordEncodingConfiguration.class,
        SecretEncryptionService.class,
        MfaApplicationService.class
})
class ApplicationServiceTests {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TenantApplicationService tenantApplicationService;

    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private RoleApplicationService roleApplicationService;

    @Autowired
    private PermissionApplicationService permissionApplicationService;

    @Autowired
    private ClientApplicationService clientApplicationService;

    @Autowired
    private GroupApplicationService groupApplicationService;

    @Autowired
    private MfaApplicationService mfaApplicationService;

    @Autowired
    private SecretEncryptionService secretEncryptionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Test
    void createsTenant() {
        Tenant tenant = tenantApplicationService.createTenant("Acme");

        Tenant loadedTenant = tenantRepository.findById(tenant.getId()).orElseThrow();

        assertThat(loadedTenant.getName()).isEqualTo("Acme");
    }

    @Test
    void createsAuditLogWhenTenantIsCreated() {
        Tenant tenant = tenantApplicationService.createTenant("Audit Tenant");

        assertThat(auditLogRepository.findByAction("TENANT_CREATED"))
                .singleElement()
                .satisfies(auditLog -> {
                    assertThat(auditLog.getTenantId()).isEqualTo(tenant.getId());
                    assertThat(auditLog.getActor()).isEqualTo("api-client");
                    assertThat(auditLog.getResourceType()).isEqualTo("TENANT");
                    assertThat(auditLog.getResourceId()).isEqualTo(tenant.getId());
                    assertThat(auditLog.getCreatedAt()).isNotNull();
                });
    }

    @Test
    void createsUserUnderTenant() {
        Tenant tenant = tenantApplicationService.createTenant("User Tenant");

        User user = userApplicationService.createUser(tenant.getId(), "alice", "Alice Example");

        User loadedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(loadedUser.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedUser.getUsername()).isEqualTo("alice");
        assertThat(loadedUser.getDisplayName()).isEqualTo("Alice Example");
        assertThat(loadedUser.getAccountStatus()).isEqualTo(AccountStatus.PENDING);
        assertThat(loadedUser.isPasswordResetRequired()).isTrue();
        assertThat(loadedUser.getCredentialsVersion()).isEqualTo(1);
    }

    @Test
    void encryptsAndDecryptsSecret() {
        String plaintext = "TOTPSECRET123";

        String ciphertext = secretEncryptionService.encrypt(plaintext);

        assertThat(ciphertext).isNotEqualTo(plaintext);
        assertThat(secretEncryptionService.decrypt(ciphertext)).isEqualTo(plaintext);
    }

    @Test
    void enrollingMfaGeneratesSecretAndStoresEncryptedSecret() {
        Tenant tenant = tenantApplicationService.createTenant("MFA Enrollment Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "mfa-user", "MFA User");

        MfaEnrollmentResult enrollment = mfaApplicationService.enrollTotp(user.getId());

        User loadedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(enrollment.userId()).isEqualTo(user.getId());
        assertThat(enrollment.secret()).isNotBlank();
        assertThat(loadedUser.isMfaEnabled()).isTrue();
        assertThat(loadedUser.getMfaSecret()).isNotEqualTo(enrollment.secret());
        assertThat(secretEncryptionService.decrypt(loadedUser.getMfaSecret())).isEqualTo(enrollment.secret());
    }

    @Test
    void verifyingValidTotpCodeSucceeds() {
        Tenant tenant = tenantApplicationService.createTenant("MFA Verify Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "verify-user", "Verify User");
        MfaEnrollmentResult enrollment = mfaApplicationService.enrollTotp(user.getId());
        String code = mfaApplicationService.generateTotpCode(enrollment.secret(), Instant.now());

        assertThat(mfaApplicationService.verifyTotp(user.getId(), code)).isTrue();
    }

    @Test
    void verifyingInvalidTotpCodeFails() {
        Tenant tenant = tenantApplicationService.createTenant("MFA Invalid Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "invalid-user", "Invalid User");
        MfaEnrollmentResult enrollment = mfaApplicationService.enrollTotp(user.getId());
        String validCode = mfaApplicationService.generateTotpCode(enrollment.secret(), Instant.now());
        String invalidCode = validCode.equals("000000") ? "000001" : "000000";

        assertThat(mfaApplicationService.verifyTotp(user.getId(), invalidCode)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "59,287082",
            "1111111109,081804",
            "1111111111,050471",
            "1234567890,005924",
            "2000000000,279037",
            "20000000000,353130"
    })
    void generatesSixDigitTotpCodesFromRfc6238Sha1Vectors(long epochSecond, String expectedCode) {
        // RFC 6238 publishes 8-digit SHA-1 values; this local MVP intentionally supports 6-digit TOTP only.
        String rfc6238Sha1Secret = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ";

        String code = mfaApplicationService.generateTotpCode(rfc6238Sha1Secret, Instant.ofEpochSecond(epochSecond));

        assertThat(code).isEqualTo(expectedCode);
    }

    @Test
    void disablingMfaClearsMfaState() {
        Tenant tenant = tenantApplicationService.createTenant("MFA Disable Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "disable-user", "Disable User");
        mfaApplicationService.enrollTotp(user.getId());

        mfaApplicationService.disableTotp(user.getId());

        User loadedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(loadedUser.isMfaEnabled()).isFalse();
        assertThat(loadedUser.getMfaSecret()).isNull();
    }

    @Test
    void recordsAuditEventsForMfaOperations() {
        Tenant tenant = tenantApplicationService.createTenant("MFA Audit Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "audit-mfa-user", "Audit MFA User");
        MfaEnrollmentResult enrollment = mfaApplicationService.enrollTotp(user.getId());
        String code = mfaApplicationService.generateTotpCode(enrollment.secret(), Instant.now());

        mfaApplicationService.verifyTotp(user.getId(), code);
        mfaApplicationService.disableTotp(user.getId());

        assertThat(auditLogRepository.findByAction("MFA_ENROLLED"))
                .singleElement()
                .satisfies(auditLog -> {
                    assertThat(auditLog.getTenantId()).isEqualTo(tenant.getId());
                    assertThat(auditLog.getResourceType()).isEqualTo("USER");
                    assertThat(auditLog.getResourceId()).isEqualTo(user.getId());
                });
        assertThat(auditLogRepository.findByAction("MFA_VERIFIED"))
                .singleElement()
                .satisfies(auditLog -> assertThat(auditLog.getResourceId()).isEqualTo(user.getId()));
        assertThat(auditLogRepository.findByAction("MFA_DISABLED"))
                .singleElement()
                .satisfies(auditLog -> assertThat(auditLog.getResourceId()).isEqualTo(user.getId()));
    }

    @Test
    void userResponseDoesNotExposeMfaSecret() {
        Tenant tenant = tenantApplicationService.createTenant("MFA Response Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "response-user", "Response User");
        MfaEnrollmentResult enrollment = mfaApplicationService.enrollTotp(user.getId());
        User loadedUser = userRepository.findById(user.getId()).orElseThrow();

        UserResponse response = UserResponse.from(loadedUser);

        assertThat(UserResponse.class.getRecordComponents())
                .extracting(RecordComponent::getName)
                .doesNotContain("mfaSecret");
        assertThat(response.toString()).doesNotContain(enrollment.secret());
    }

    @Test
    void userResponseDoesNotExposePasswordCredentialFields() {
        Tenant tenant = tenantApplicationService.createTenant("Password Response Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "password-response-user", "Password Response");
        user.setPasswordHash("$2a$10$sensitiveHash");
        user.setPasswordUpdatedAt(Instant.parse("2026-01-01T00:00:00Z"));
        user.setPasswordResetRequired(false);
        user.setCredentialsVersion(3);
        User savedUser = userRepository.save(user);

        UserResponse response = UserResponse.from(savedUser);

        assertThat(UserResponse.class.getRecordComponents())
                .extracting(RecordComponent::getName)
                .doesNotContain("passwordHash", "passwordUpdatedAt", "passwordResetRequired", "credentialsVersion");
        assertThat(response.toString()).doesNotContain("$2a$10$sensitiveHash");
        assertThat(response.accountStatus()).isEqualTo(AccountStatus.PENDING);
    }

    @Test
    void settingInitialPasswordStoresEncodedHashAndActivatesUser() {
        Tenant tenant = tenantApplicationService.createTenant("Initial Password Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "initial-password-user", "Initial Password");
        Instant beforePasswordSet = Instant.now();

        User updatedUser = userApplicationService.setInitialPassword(user.getId(), "initial-password-123");

        User loadedUser = userRepository.findById(updatedUser.getId()).orElseThrow();
        assertThat(loadedUser.getPasswordHash()).isNotBlank();
        assertThat(loadedUser.getPasswordHash()).isNotEqualTo("initial-password-123");
        assertThat(passwordEncoder.matches("initial-password-123", loadedUser.getPasswordHash())).isTrue();
        assertThat(loadedUser.getPasswordUpdatedAt()).isAfterOrEqualTo(beforePasswordSet);
        assertThat(loadedUser.isPasswordResetRequired()).isFalse();
        assertThat(loadedUser.getCredentialsVersion()).isEqualTo(2);
        assertThat(loadedUser.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void updatingPasswordChangesHashAndIncrementsCredentialsVersion() {
        Tenant tenant = tenantApplicationService.createTenant("Update Password Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "update-password-user", "Update Password");
        User initialPasswordUser = userApplicationService.setInitialPassword(user.getId(), "first-password-123");
        String initialPasswordHash = initialPasswordUser.getPasswordHash();
        Instant initialPasswordUpdatedAt = initialPasswordUser.getPasswordUpdatedAt();

        User updatedUser = userApplicationService.updatePassword(user.getId(), "second-password-123");

        assertThat(updatedUser.getPasswordHash()).isNotEqualTo(initialPasswordHash);
        assertThat(passwordEncoder.matches("second-password-123", updatedUser.getPasswordHash())).isTrue();
        assertThat(updatedUser.getCredentialsVersion()).isEqualTo(3);
        assertThat(updatedUser.getPasswordUpdatedAt()).isAfterOrEqualTo(initialPasswordUpdatedAt);
        assertThat(updatedUser.isPasswordResetRequired()).isFalse();
    }

    @Test
    void passwordResetRequirementCanBeSetAndClearedWithoutChangingCredentialsVersion() {
        Tenant tenant = tenantApplicationService.createTenant("Password Reset Flag Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "reset-flag-user", "Reset Flag");
        User passwordUser = userApplicationService.setInitialPassword(user.getId(), "reset-flag-password");

        User resetRequiredUser = userApplicationService.requirePasswordReset(passwordUser.getId());
        assertThat(resetRequiredUser.isPasswordResetRequired()).isTrue();
        assertThat(resetRequiredUser.getCredentialsVersion()).isEqualTo(2);

        User clearedUser = userApplicationService.clearPasswordResetRequired(passwordUser.getId());
        assertThat(clearedUser.isPasswordResetRequired()).isFalse();
        assertThat(clearedUser.getCredentialsVersion()).isEqualTo(2);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "short"})
    void invalidPasswordInputIsRejected(String invalidPassword) {
        Tenant tenant = tenantApplicationService.createTenant("Invalid Password Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "invalid-password-user", "Invalid Password");

        assertThatThrownBy(() -> userApplicationService.setInitialPassword(user.getId(), invalidPassword))
                .isInstanceOf(PasswordValidationException.class);
    }

    @Test
    void passwordAuditLogsDoNotIncludeRawPasswordOrHash() {
        Tenant tenant = tenantApplicationService.createTenant("Password Audit Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "password-audit-user", "Password Audit");
        String rawPassword = "audit-password-123";

        User updatedUser = userApplicationService.setInitialPassword(user.getId(), rawPassword);

        assertThat(auditLogRepository.findByAction("USER_PASSWORD_SET"))
                .singleElement()
                .satisfies(auditLog -> {
                    assertThat(auditLog.getTenantId()).isEqualTo(tenant.getId());
                    assertThat(auditLog.getResourceType()).isEqualTo("USER");
                    assertThat(auditLog.getResourceId()).isEqualTo(user.getId());
                    assertAuditLogDoesNotContain(auditLog, rawPassword);
                    assertAuditLogDoesNotContain(auditLog, updatedUser.getPasswordHash());
                });
    }

    @Test
    void createsRoleUnderTenant() {
        Tenant tenant = tenantApplicationService.createTenant("Role Tenant");

        Role role = roleApplicationService.createRole(tenant.getId(), "admin");

        Role loadedRole = roleRepository.findById(role.getId()).orElseThrow();
        assertThat(loadedRole.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedRole.getName()).isEqualTo("admin");
    }

    @Test
    void createsPermission() {
        Tenant tenant = tenantApplicationService.createTenant("Permission Tenant");

        Permission permission = permissionApplicationService.createPermission(tenant.getId(), "clients:read");

        Permission loadedPermission = permissionRepository.findById(permission.getId()).orElseThrow();

        assertThat(loadedPermission.getName()).isEqualTo("clients:read");
    }

    @Test
    void assignsRoleToUser() {
        Tenant tenant = tenantApplicationService.createTenant("Assignment Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "bob", "Bob Example");
        Role role = roleApplicationService.createRole(tenant.getId(), "operator");

        userApplicationService.assignRoleToUser(user.getId(), role.getId());

        User loadedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(loadedUser.getRoles())
                .extracting(Role::getName)
                .containsExactly("operator");
    }

    @Test
    void createsAuditLogWhenRoleIsAssignedToUser() {
        Tenant tenant = tenantApplicationService.createTenant("Audit Assignment Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "carol", "Carol Example");
        Role role = roleApplicationService.createRole(tenant.getId(), "reviewer");

        userApplicationService.assignRoleToUser(user.getId(), role.getId());

        assertThat(auditLogRepository.findByAction("ROLE_ASSIGNED_TO_USER"))
                .singleElement()
                .satisfies(auditLog -> {
                    assertThat(auditLog.getTenantId()).isEqualTo(tenant.getId());
                    assertThat(auditLog.getActor()).isEqualTo("api-client");
                    assertThat(auditLog.getResourceType()).isEqualTo("USER");
                    assertThat(auditLog.getResourceId()).isEqualTo(user.getId());
                });
    }

    @Test
    void shouldRejectAssigningRoleFromDifferentTenantToUser() {
        Tenant userTenant = tenantApplicationService.createTenant("User Boundary Tenant");
        Tenant roleTenant = tenantApplicationService.createTenant("Role Boundary Tenant");
        User user = userApplicationService.createUser(userTenant.getId(), "mallory", "Mallory Example");
        Role role = roleApplicationService.createRole(roleTenant.getId(), "external-admin");

        assertThatThrownBy(() -> userApplicationService.assignRoleToUser(user.getId(), role.getId()))
                .isInstanceOf(TenantBoundaryViolationException.class)
                .hasMessage("User and role must belong to the same tenant");

        User loadedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(loadedUser.getRoles()).isEmpty();
    }

    @Test
    void assignsPermissionToRole() {
        Tenant tenant = tenantApplicationService.createTenant("Permission Assignment Tenant");
        Role role = roleApplicationService.createRole(tenant.getId(), "auditor");
        Permission permission = permissionApplicationService.createPermission(tenant.getId(), "users:read");

        roleApplicationService.assignPermissionToRole(role.getId(), permission.getId());

        Role loadedRole = roleRepository.findById(role.getId()).orElseThrow();
        assertThat(loadedRole.getPermissions())
                .extracting(Permission::getName)
                .containsExactly("users:read");
    }

    @Test
    void createsClientUnderTenant() {
        Tenant tenant = tenantApplicationService.createTenant("Client Tenant");

        Client client = clientApplicationService.createClient(tenant.getId(), "portal", "Portal");

        Client loadedClient = clientRepository.findById(client.getId()).orElseThrow();
        assertThat(loadedClient.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedClient.getClientId()).isEqualTo("portal");
        assertThat(loadedClient.getName()).isEqualTo("Portal");
    }

    @Test
    void createsGroupUnderTenant() {
        Tenant tenant = tenantApplicationService.createTenant("Group Tenant");

        Group group = groupApplicationService.createGroup(tenant.getId(), "engineering");

        Group loadedGroup = groupRepository.findById(group.getId()).orElseThrow();
        assertThat(loadedGroup.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedGroup.getName()).isEqualTo("engineering");
    }

    @Test
    void addsUserToGroupUnderSameTenant() {
        Tenant tenant = tenantApplicationService.createTenant("Group Member Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "member-user", "Member User");
        Group group = groupApplicationService.createGroup(tenant.getId(), "members");

        groupApplicationService.addUserToGroup(group.getId(), user.getId());

        Group loadedGroup = groupRepository.findById(group.getId()).orElseThrow();
        assertThat(loadedGroup.getUsers())
                .extracting(User::getUsername)
                .containsExactly("member-user");
    }

    @Test
    void removesUserFromGroupUnderSameTenant() {
        Tenant tenant = tenantApplicationService.createTenant("Group Remove Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "remove-user", "Remove User");
        Group group = groupApplicationService.createGroup(tenant.getId(), "removable");
        groupApplicationService.addUserToGroup(group.getId(), user.getId());

        groupApplicationService.removeUserFromGroup(group.getId(), user.getId());

        Group loadedGroup = groupRepository.findById(group.getId()).orElseThrow();
        assertThat(loadedGroup.getUsers()).isEmpty();
    }

    @Test
    void rejectsRemovingUserFromDifferentTenantFromGroup() {
        Tenant groupTenant = tenantApplicationService.createTenant("Group Remove Boundary Tenant");
        Tenant userTenant = tenantApplicationService.createTenant("Remove Member Boundary Tenant");
        Group group = groupApplicationService.createGroup(groupTenant.getId(), "remove-boundary-group");
        User user = userApplicationService.createUser(userTenant.getId(), "remove-external-member", "Remove External");

        assertThatThrownBy(() -> groupApplicationService.removeUserFromGroup(group.getId(), user.getId()))
                .isInstanceOf(TenantBoundaryViolationException.class)
                .hasMessage("User and group must belong to the same tenant");
    }

    @Test
    void removingUserWhoIsNotGroupMemberDoesNotRecordRemovalAuditEvent() {
        Tenant tenant = tenantApplicationService.createTenant("No Op Remove Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "not-member-user", "Not Member User");
        Group group = groupApplicationService.createGroup(tenant.getId(), "no-op-removable");

        groupApplicationService.removeUserFromGroup(group.getId(), user.getId());

        Group loadedGroup = groupRepository.findById(group.getId()).orElseThrow();
        assertThat(loadedGroup.getUsers()).isEmpty();
        assertThat(auditLogRepository.findByAction("USER_REMOVED_FROM_GROUP")).isEmpty();
    }

    @Test
    void rejectsAddingUserFromDifferentTenantToGroup() {
        Tenant groupTenant = tenantApplicationService.createTenant("Group Boundary Tenant");
        Tenant userTenant = tenantApplicationService.createTenant("Member Boundary Tenant");
        Group group = groupApplicationService.createGroup(groupTenant.getId(), "boundary-group");
        User user = userApplicationService.createUser(userTenant.getId(), "external-member", "External Member");

        assertThatThrownBy(() -> groupApplicationService.addUserToGroup(group.getId(), user.getId()))
                .isInstanceOf(TenantBoundaryViolationException.class)
                .hasMessage("User and group must belong to the same tenant");

        Group loadedGroup = groupRepository.findById(group.getId()).orElseThrow();
        assertThat(loadedGroup.getUsers()).isEmpty();
    }

    @Test
    void recordsAuditEventsForGroupOperations() {
        Tenant tenant = tenantApplicationService.createTenant("Group Audit Tenant");
        User user = userApplicationService.createUser(tenant.getId(), "group-audit-user", "Group Audit User");
        Group group = groupApplicationService.createGroup(tenant.getId(), "audited-group");

        groupApplicationService.addUserToGroup(group.getId(), user.getId());
        groupApplicationService.removeUserFromGroup(group.getId(), user.getId());

        assertThat(auditLogRepository.findByAction("GROUP_CREATED"))
                .singleElement()
                .satisfies(auditLog -> {
                    assertThat(auditLog.getTenantId()).isEqualTo(tenant.getId());
                    assertThat(auditLog.getResourceType()).isEqualTo("GROUP");
                    assertThat(auditLog.getResourceId()).isEqualTo(group.getId());
                });
        assertThat(auditLogRepository.findByAction("USER_ADDED_TO_GROUP"))
                .singleElement()
                .satisfies(auditLog -> assertThat(auditLog.getResourceId()).isEqualTo(group.getId()));
        assertThat(auditLogRepository.findByAction("USER_REMOVED_FROM_GROUP"))
                .singleElement()
                .satisfies(auditLog -> assertThat(auditLog.getResourceId()).isEqualTo(group.getId()));
    }

    @Test
    void auditLogDoesNotIncludeSensitiveData() {
        Tenant tenant = tenantApplicationService.createTenant("Sensitive Audit Tenant");

        clientApplicationService.createClient(tenant.getId(), "secret-client-id", "Client With Secret");

        assertThat(auditLogRepository.findByAction("CLIENT_CREATED"))
                .singleElement()
                .satisfies(this::assertNoSensitiveData);
    }

    private void assertNoSensitiveData(AuditLog auditLog) {
        assertThat(auditLog.getActor())
                .doesNotContainIgnoringCase("secret")
                .doesNotContainIgnoringCase("token")
                .doesNotContainIgnoringCase("password");
        assertThat(auditLog.getAction())
                .doesNotContainIgnoringCase("secret")
                .doesNotContainIgnoringCase("token")
                .doesNotContainIgnoringCase("password");
        assertThat(auditLog.getResourceType())
                .doesNotContainIgnoringCase("secret")
                .doesNotContainIgnoringCase("token")
                .doesNotContainIgnoringCase("password");
    }

    private void assertAuditLogDoesNotContain(AuditLog auditLog, String sensitiveValue) {
        assertThat(auditLog.getActor()).doesNotContain(sensitiveValue);
        assertThat(auditLog.getAction()).doesNotContain(sensitiveValue);
        assertThat(auditLog.getResourceType()).doesNotContain(sensitiveValue);
        assertThat(auditLog.getResourceId().toString()).doesNotContain(sensitiveValue);
    }
}
