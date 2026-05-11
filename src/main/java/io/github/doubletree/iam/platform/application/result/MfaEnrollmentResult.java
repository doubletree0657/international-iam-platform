package io.github.doubletree.iam.platform.application.result;

import java.util.UUID;

public record MfaEnrollmentResult(UUID userId, String secret) {
}
