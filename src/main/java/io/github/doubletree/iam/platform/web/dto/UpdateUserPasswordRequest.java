package io.github.doubletree.iam.platform.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserPasswordRequest(
        @NotBlank String newPassword,
        Boolean passwordResetRequired) {
}
