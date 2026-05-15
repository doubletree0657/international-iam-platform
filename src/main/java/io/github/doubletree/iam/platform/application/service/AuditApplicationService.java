package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.domain.AuditActorType;
import io.github.doubletree.iam.platform.domain.AuditLog;
import io.github.doubletree.iam.platform.domain.AuditResult;
import io.github.doubletree.iam.platform.repository.AuditLogRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuditApplicationService {

    static final String DEFAULT_ACTOR = "api-client";

    private final AuditLogRepository auditLogRepository;

    public AuditApplicationService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog recordEvent(UUID tenantId, String action, String resourceType, UUID resourceId) {
        return auditLogRepository.save(AuditLog.record(
                tenantId,
                AuditActorType.API_CLIENT,
                null,
                action,
                resourceType,
                resourceId,
                AuditResult.SUCCESS));
    }
}
