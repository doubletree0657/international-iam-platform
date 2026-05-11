package io.github.doubletree.iam.platform.application.exception;

public class TenantBoundaryViolationException extends RuntimeException {

    public TenantBoundaryViolationException(String message) {
        super(message);
    }
}
