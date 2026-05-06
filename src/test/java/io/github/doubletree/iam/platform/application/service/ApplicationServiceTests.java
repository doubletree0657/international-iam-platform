package io.github.doubletree.iam.platform.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.doubletree.iam.platform.domain.Client;
import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.domain.Role;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.domain.User;
import io.github.doubletree.iam.platform.repository.ClientRepository;
import io.github.doubletree.iam.platform.repository.PermissionRepository;
import io.github.doubletree.iam.platform.repository.RoleRepository;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import io.github.doubletree.iam.platform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
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
        ClientApplicationService.class
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
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ClientRepository clientRepository;

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
    void createsUserUnderTenant() {
        Tenant tenant = tenantApplicationService.createTenant("User Tenant");

        User user = userApplicationService.createUser(tenant.getId(), "alice", "Alice Example");

        User loadedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(loadedUser.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedUser.getUsername()).isEqualTo("alice");
        assertThat(loadedUser.getDisplayName()).isEqualTo("Alice Example");
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
        Permission permission = permissionApplicationService.createPermission("clients:read");

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
    void assignsPermissionToRole() {
        Tenant tenant = tenantApplicationService.createTenant("Permission Assignment Tenant");
        Role role = roleApplicationService.createRole(tenant.getId(), "auditor");
        Permission permission = permissionApplicationService.createPermission("users:read");

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
}
