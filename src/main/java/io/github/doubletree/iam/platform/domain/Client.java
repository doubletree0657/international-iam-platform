package io.github.doubletree.iam.platform.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Client represents an OAuth2 client registration owned by a tenant.
 */
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType clientType = ClientType.CONFIDENTIAL;

    private String clientSecretHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status = ClientStatus.ACTIVE;

    @Column(nullable = false)
    private boolean requirePkce = true;

    @Column(nullable = false)
    private boolean requireConsent = true;

    @ElementCollection
    @CollectionTable(name = "client_redirect_uris", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "redirect_uri", nullable = false)
    private Set<String> redirectUris = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "client_grant_types", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type", nullable = false)
    private Set<String> grantTypes = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "client_scopes", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "scope", nullable = false)
    private Set<String> scopes = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "client_authentication_methods", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "authentication_method", nullable = false)
    private Set<String> authenticationMethods = new LinkedHashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    protected Client() {
    }

    public static Client create(Tenant tenant, String clientId, String name) {
        Client client = new Client();
        client.setTenant(tenant);
        client.setClientId(clientId);
        client.setClientName(name);
        client.getGrantTypes().add("authorization_code");
        client.getScopes().add("iam.read");
        client.getAuthenticationMethods().add("client_secret_basic");
        return client;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return clientName;
    }

    public void setName(String name) {
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getClientSecretHash() {
        return clientSecretHash;
    }

    public void setClientSecretHash(String clientSecretHash) {
        this.clientSecretHash = clientSecretHash;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public boolean isRequirePkce() {
        return requirePkce;
    }

    public void setRequirePkce(boolean requirePkce) {
        this.requirePkce = requirePkce;
    }

    public boolean isRequireConsent() {
        return requireConsent;
    }

    public void setRequireConsent(boolean requireConsent) {
        this.requireConsent = requireConsent;
    }

    public Set<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(Set<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public Set<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(Set<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Set<String> getAuthenticationMethods() {
        return authenticationMethods;
    }

    public void setAuthenticationMethods(Set<String> authenticationMethods) {
        this.authenticationMethods = authenticationMethods;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
