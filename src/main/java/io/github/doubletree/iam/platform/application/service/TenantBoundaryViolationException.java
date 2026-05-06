package io.github.doubletree.iam.platform.application.service;

public class TenantBoundaryViolationException extends RuntimeException {

    public TenantBoundaryViolationException(String message) {
        super(message);
    }
}
