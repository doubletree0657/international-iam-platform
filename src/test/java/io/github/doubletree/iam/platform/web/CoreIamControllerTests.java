package io.github.doubletree.iam.platform.web;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.doubletree.iam.platform.application.service.ClientApplicationService;
import io.github.doubletree.iam.platform.application.service.EntityNotFoundException;
import io.github.doubletree.iam.platform.application.service.PermissionApplicationService;
import io.github.doubletree.iam.platform.application.service.RoleApplicationService;
import io.github.doubletree.iam.platform.application.service.TenantApplicationService;
import io.github.doubletree.iam.platform.application.service.UserApplicationService;
import io.github.doubletree.iam.platform.domain.Client;
import io.github.doubletree.iam.platform.domain.Permission;
import io.github.doubletree.iam.platform.domain.Role;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.domain.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        TenantController.class,
        UserController.class,
        RoleController.class,
        PermissionController.class,
        ClientController.class,
        RestExceptionHandler.class
})
class CoreIamControllerTests {

    private static final UUID TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID ROLE_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID PERMISSION_ID = UUID.fromString("00000000-0000-0000-0000-000000000004");
    private static final UUID CLIENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000005");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TenantApplicationService tenantApplicationService;

    @MockitoBean
    private UserApplicationService userApplicationService;

    @MockitoBean
    private RoleApplicationService roleApplicationService;

    @MockitoBean
    private PermissionApplicationService permissionApplicationService;

    @MockitoBean
    private ClientApplicationService clientApplicationService;

    @Test
    void createsTenant() throws Exception {
        when(tenantApplicationService.createTenant(eq("Acme")))
                .thenReturn(tenant("Acme"));

        mockMvc.perform(post("/api/tenants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Acme"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TENANT_ID.toString()))
                .andExpect(jsonPath("$.name").value("Acme"));
    }

    @Test
    void createsUserUnderTenant() throws Exception {
        when(userApplicationService.createUser(eq(TENANT_ID), eq("alice"), eq("Alice Example")))
                .thenReturn(user("alice", "Alice Example"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tenantId":"00000000-0000-0000-0000-000000000001",
                                  "username":"alice",
                                  "displayName":"Alice Example"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.tenantId").value(TENANT_ID.toString()))
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.displayName").value("Alice Example"));
    }

    @Test
    void createsRoleUnderTenant() throws Exception {
        when(roleApplicationService.createRole(eq(TENANT_ID), eq("admin")))
                .thenReturn(role("admin"));

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tenantId":"00000000-0000-0000-0000-000000000001",
                                  "name":"admin"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ROLE_ID.toString()))
                .andExpect(jsonPath("$.tenantId").value(TENANT_ID.toString()))
                .andExpect(jsonPath("$.name").value("admin"));
    }

    @Test
    void createsPermission() throws Exception {
        when(permissionApplicationService.createPermission(eq("clients:read")))
                .thenReturn(permission("clients:read"));

        mockMvc.perform(post("/api/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"clients:read"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PERMISSION_ID.toString()))
                .andExpect(jsonPath("$.name").value("clients:read"));
    }

    @Test
    void assignsRoleToUser() throws Exception {
        User user = user("bob", "Bob Example");
        user.getRoles().add(role("operator"));
        when(userApplicationService.assignRoleToUser(eq(USER_ID), eq(ROLE_ID)))
                .thenReturn(user);

        mockMvc.perform(post("/api/users/{userId}/roles/{roleId}", USER_ID, ROLE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.roleIds[0]").value(ROLE_ID.toString()));
    }

    @Test
    void assignsPermissionToRole() throws Exception {
        Role role = role("auditor");
        role.getPermissions().add(permission("users:read"));
        when(roleApplicationService.assignPermissionToRole(eq(ROLE_ID), eq(PERMISSION_ID)))
                .thenReturn(role);

        mockMvc.perform(post("/api/roles/{roleId}/permissions/{permissionId}", ROLE_ID, PERMISSION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ROLE_ID.toString()))
                .andExpect(jsonPath("$.permissionIds[0]").value(PERMISSION_ID.toString()));
    }

    @Test
    void createsClientUnderTenant() throws Exception {
        when(clientApplicationService.createClient(eq(TENANT_ID), eq("portal"), eq("Portal")))
                .thenReturn(client("portal", "Portal"));

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tenantId":"00000000-0000-0000-0000-000000000001",
                                  "clientId":"portal",
                                  "name":"Portal"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(CLIENT_ID.toString()))
                .andExpect(jsonPath("$.tenantId").value(TENANT_ID.toString()))
                .andExpect(jsonPath("$.clientId").value("portal"))
                .andExpect(jsonPath("$.name").value("Portal"));
    }

    @Test
    void entityNotFoundReturnsNotFound() throws Exception {
        when(userApplicationService.createUser(eq(TENANT_ID), eq("missing"), eq("Missing User")))
                .thenThrow(new EntityNotFoundException("Tenant not found: " + TENANT_ID));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tenantId":"00000000-0000-0000-0000-000000000001",
                                  "username":"missing",
                                  "displayName":"Missing User"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("not_found"))
                .andExpect(jsonPath("$.message").value("Tenant not found: " + TENANT_ID));
    }

    @Test
    void validationErrorReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/tenants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation_error"))
                .andExpect(jsonPath("$.message").value("Request validation failed"));
    }

    private Tenant tenant(String name) {
        Tenant tenant = Tenant.create(name);
        tenant.setId(TENANT_ID);
        return tenant;
    }

    private User user(String username, String displayName) {
        User user = User.create(tenant("Test Tenant"), username, displayName);
        user.setId(USER_ID);
        return user;
    }

    private Role role(String name) {
        Role role = Role.create(tenant("Test Tenant"), name);
        role.setId(ROLE_ID);
        return role;
    }

    private Permission permission(String name) {
        Permission permission = Permission.create(name);
        permission.setId(PERMISSION_ID);
        return permission;
    }

    private Client client(String clientId, String name) {
        Client client = Client.create(tenant("Test Tenant"), clientId, name);
        client.setId(CLIENT_ID);
        return client;
    }
}
