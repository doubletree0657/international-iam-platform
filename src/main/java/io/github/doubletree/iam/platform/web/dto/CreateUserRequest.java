package io.github.doubletree.iam.platform.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateUserRequest(
        @NotNull UUID tenantId,
        @NotBlank String username,
        @NotBlank String displayName) {
}
