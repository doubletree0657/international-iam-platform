package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantApplicationService {

    private final TenantRepository tenantRepository;

    public TenantApplicationService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public Tenant createTenant(String name) {
        return tenantRepository.save(Tenant.create(name));
    }
}
