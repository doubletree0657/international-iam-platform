package io.github.doubletree.iam.platform.web;

import io.github.doubletree.iam.platform.application.service.TenantApplicationService;
import io.github.doubletree.iam.platform.domain.Tenant;
import io.github.doubletree.iam.platform.web.dto.CreateTenantRequest;
import io.github.doubletree.iam.platform.web.dto.TenantResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantApplicationService tenantApplicationService;

    public TenantController(TenantApplicationService tenantApplicationService) {
        this.tenantApplicationService = tenantApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TenantResponse createTenant(@Valid @RequestBody CreateTenantRequest request) {
        Tenant tenant = tenantApplicationService.createTenant(request.name());
        return TenantResponse.from(tenant);
    }
}
