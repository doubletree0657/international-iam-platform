package io.github.doubletree.iam.platform.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTenantRequest(@NotBlank String name) {
}
