package io.github.doubletree.iam.platform.web;

import io.github.doubletree.iam.platform.application.service.ClientApplicationService;
import io.github.doubletree.iam.platform.domain.Client;
import io.github.doubletree.iam.platform.web.dto.ClientResponse;
import io.github.doubletree.iam.platform.web.dto.CreateClientRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientApplicationService clientApplicationService;

    public ClientController(ClientApplicationService clientApplicationService) {
        this.clientApplicationService = clientApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse createClient(@Valid @RequestBody CreateClientRequest request) {
        Client client = clientApplicationService.createClient(request.tenantId(), request.clientId(), request.name());
        return ClientResponse.from(client);
    }
}
