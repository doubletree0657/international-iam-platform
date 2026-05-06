package io.github.doubletree.iam.internationaliamplatform.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.doubletree.iam.internationaliamplatform.repository.ClientRepository;
import io.github.doubletree.iam.internationaliamplatform.repository.PermissionRepository;
import io.github.doubletree.iam.internationaliamplatform.repository.RoleRepository;
import io.github.doubletree.iam.internationaliamplatform.repository.TenantRepository;
import io.github.doubletree.iam.internationaliamplatform.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DomainPersistenceTests {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

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
    private EntityManager entityManager;

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Test
    void tenantCanBeSavedAndLoaded() {
        Tenant tenant = tenantRepository.save(tenant("Acme"));

        flushAndClear();

        Tenant loadedTenant = tenantRepository.findById(tenant.getId()).orElseThrow();

        assertThat(loadedTenant.getName()).isEqualTo("Acme");
        assertThat(loadedTenant.getCreatedAt()).isNotNull();
    }

    @Test
    void userBelongsToTenant() {
        Tenant tenant = tenantRepository.save(tenant("User Tenant"));
        User user = userRepository.save(user(tenant, "alice", "Alice Example"));

        flushAndClear();

        User loadedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(loadedUser.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedUser.getUsername()).isEqualTo("alice");
    }

    @Test
    void roleBelongsToTenant() {
        Tenant tenant = tenantRepository.save(tenant("Role Tenant"));
        Role role = roleRepository.save(role(tenant, "admin"));

        flushAndClear();

        Role loadedRole = roleRepository.findById(role.getId()).orElseThrow();

        assertThat(loadedRole.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedRole.getName()).isEqualTo("admin");
    }

    @Test
    void userCanHaveRoles() {
        Tenant tenant = tenantRepository.save(tenant("User Role Tenant"));
        Role role = roleRepository.save(role(tenant, "operator"));
        User user = user(tenant, "bob", "Bob Example");
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);

        flushAndClear();

        User loadedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        assertThat(loadedUser.getRoles())
                .extracting(Role::getName)
                .containsExactly("operator");
    }

    @Test
    void roleCanHavePermissions() {
        Tenant tenant = tenantRepository.save(tenant("Role Permission Tenant"));
        Permission permission = permissionRepository.save(permission("clients:read"));
        Role role = role(tenant, "auditor");
        role.getPermissions().add(permission);
        Role savedRole = roleRepository.save(role);

        flushAndClear();

        Role loadedRole = roleRepository.findById(savedRole.getId()).orElseThrow();

        assertThat(loadedRole.getPermissions())
                .extracting(Permission::getName)
                .containsExactly("clients:read");
    }

    @Test
    void clientBelongsToTenant() {
        Tenant tenant = tenantRepository.save(tenant("Client Tenant"));
        Client client = clientRepository.save(client(tenant, "portal", "Portal"));

        flushAndClear();

        Client loadedClient = clientRepository.findById(client.getId()).orElseThrow();

        assertThat(loadedClient.getTenant().getId()).isEqualTo(tenant.getId());
        assertThat(loadedClient.getClientId()).isEqualTo("portal");
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    private Tenant tenant(String name) {
        Tenant tenant = new Tenant();
        tenant.setName(name);
        return tenant;
    }

    private User user(Tenant tenant, String username, String displayName) {
        User user = new User();
        user.setTenant(tenant);
        user.setUsername(username);
        user.setDisplayName(displayName);
        return user;
    }

    private Role role(Tenant tenant, String name) {
        Role role = new Role();
        role.setTenant(tenant);
        role.setName(name);
        return role;
    }

    private Permission permission(String name) {
        Permission permission = new Permission();
        permission.setName(name);
        return permission;
    }

    private Client client(Tenant tenant, String clientId, String name) {
        Client client = new Client();
        client.setTenant(tenant);
        client.setClientId(clientId);
        client.setName(name);
        return client;
    }
}
