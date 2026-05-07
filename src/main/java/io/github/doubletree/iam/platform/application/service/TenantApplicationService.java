package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantApplicationService {

    private final TenantRepository tenantRepository;
    private final AuditApplicationService auditApplicationService;

    public TenantApplicationService(TenantRepository tenantRepository, AuditApplicationService auditApplicationService) {
        this.tenantRepository = tenantRepository;
        this.auditApplicationService = auditApplicationService;
    }

    @Transactional
    public Tenant createTenant(String name) {
        Tenant tenant = tenantRepository.save(Tenant.create(name));
        auditApplicationService.recordEvent(tenant.getId(), "TENANT_CREATED", "TENANT", tenant.getId());
        return tenant;
    }
}
