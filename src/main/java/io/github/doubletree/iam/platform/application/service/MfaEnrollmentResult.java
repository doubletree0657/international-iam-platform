package io.github.doubletree.iam.platform.application.service;

import java.util.UUID;

public record MfaEnrollmentResult(UUID userId, String secret) {
}
