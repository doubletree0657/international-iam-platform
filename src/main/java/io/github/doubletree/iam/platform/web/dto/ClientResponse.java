package io.github.doubletree.iam.platform.web.dto;

import io.github.doubletree.iam.platform.domain.Client;
import io.github.doubletree.iam.platform.domain.ClientStatus;
import io.github.doubletree.iam.platform.domain.ClientType;
import java.util.Set;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        UUID tenantId,
        String clientId,
        String name,
        ClientType clientType,
        ClientStatus status,
        boolean requirePkce,
        boolean requireConsent,
        Set<String> redirectUris,
        Set<String> grantTypes,
        Set<String> scopes,
        Set<String> authenticationMethods) {

    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getTenant().getId(),
                client.getClientId(),
                client.getClientName(),
                client.getClientType(),
                client.getStatus(),
                client.isRequirePkce(),
                client.isRequireConsent(),
                client.getRedirectUris(),
                client.getGrantTypes(),
                client.getScopes(),
                client.getAuthenticationMethods());
    }
}
