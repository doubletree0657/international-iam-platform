package io.github.doubletree.iam.platform.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateClientRequest(
        @NotNull UUID tenantId,
        @NotBlank String clientId,
        @NotBlank String name) {
}
