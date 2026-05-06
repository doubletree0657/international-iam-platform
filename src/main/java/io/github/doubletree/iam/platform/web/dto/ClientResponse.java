package io.github.doubletree.iam.platform.web.dto;

import io.github.doubletree.iam.platform.domain.Client;
import java.util.UUID;

public record ClientResponse(UUID id, UUID tenantId, String clientId, String name) {

    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getTenant().getId(),
                client.getClientId(),
                client.getName());
    }
}
