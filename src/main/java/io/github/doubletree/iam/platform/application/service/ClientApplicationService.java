package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.application.exception.EntityNotFoundException;
import io.github.doubletree.iam.platform.domain.Client;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.repository.ClientRepository;
import io.github.doubletree.iam.platform.repository.TenantRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientApplicationService {

    private final ClientRepository clientRepository;
    private final TenantRepository tenantRepository;
    private final AuditApplicationService auditApplicationService;

    public ClientApplicationService(
            ClientRepository clientRepository,
            TenantRepository tenantRepository,
            AuditApplicationService auditApplicationService) {
        this.clientRepository = clientRepository;
        this.tenantRepository = tenantRepository;
        this.auditApplicationService = auditApplicationService;
    }

    @Transactional
    public Client createClient(UUID tenantId, String clientId, String name) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found: " + tenantId));

        Client candidate = Client.create(tenant, clientId, name);
        candidate.validateRegistration();
        Client client = clientRepository.save(candidate);
        auditApplicationService.recordEvent(tenant.getId(), "CLIENT_CREATED", "CLIENT", client.getId());
        return client;
    }
}
