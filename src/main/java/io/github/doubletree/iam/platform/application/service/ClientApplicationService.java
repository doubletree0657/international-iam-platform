package io.github.doubletree.iam.platform.application.service;

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

    public ClientApplicationService(ClientRepository clientRepository, TenantRepository tenantRepository) {
        this.clientRepository = clientRepository;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public Client createClient(UUID tenantId, String clientId, String name) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found: " + tenantId));

        return clientRepository.save(Client.create(tenant, clientId, name));
    }
}
