package io.github.doubletree.iam.platform.web.dto;

import io.github.doubletree.iam.platform.domain.ClientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record CreateClientRequest(
        @NotNull UUID tenantId,
        @NotBlank String clientId,
        @NotBlank String name,
        ClientType clientType,
        Boolean requirePkce,
        Boolean requireConsent,
        Set<String> redirectUris,
        Set<String> grantTypes,
        Set<String> scopes,
        Set<String> authenticationMethods) {
}
