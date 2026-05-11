# Roadmap

## Current Foundation Stage Status

The IAM backend foundation stage is complete.

This means the project has a working backend foundation for discussing IAM domain design, persistence, application services, REST APIs, OAuth2/JWT concepts, audit logging, MFA, SCIM foundations, OpenAPI documentation, and CI/CD practice.

The project is still a prototype. No formal `v0.1.0` release is being published yet.

## Current Phase

Current phase:

- Documentation cleanup after IAM backend foundation stage: Done

Next phase:

- Phase 17 — User Authentication and Login Foundation

## Completed Phases

### Phase 1 — Project Baseline

Status: Done

Goal:

- Create the initial Spring Boot project.
- Add README.
- Add Docker Compose.
- Add health check endpoint.
- Verify local development environment.

### Phase 2 — Core Domain Model

Status: Done

Goal:

- Define the core IAM entities.
- Establish the basic identity and access model.
- Keep the model simple and explainable.

Core entities:

- Tenant.
- User.
- Client.
- Role.
- Permission.

### Phase 3 — Database Migration

Status: Done

Goal:

- Introduce Flyway.
- Create the initial database schema.
- Keep database changes version-controlled.

### Phase 4 — Repository Layer and Persistence Tests

Status: Done

Goal:

- Add Spring Data JPA repositories.
- Verify entity mappings with PostgreSQL.
- Use Testcontainers for realistic persistence tests.

### Phase 5 — Application Service Boundary / Use Case Layer

Status: Done

Goal:

- Add minimal application services.
- Define clear use case boundaries.
- Prepare for REST APIs and security integration.

### Phase 6 — REST API Layer

Status: Done

Goal:

- Add basic REST APIs.
- Add DTOs.
- Add validation.
- Add basic error handling.

### Phase 6.5 — Tenant Boundary Validation

Status: Done

Goal:

- Prevent cross-tenant role assignment.
- Keep tenant boundary checks in the application service layer.
- Prepare safer domain behavior before OAuth2 work.

### Phase 7 — OAuth2 Authorization Server

Status: Done

Goal:

- Introduce Spring Authorization Server.
- Support registered clients.
- Prepare token issuing capability.
- Build the foundation for OAuth2 flows.

### Phase 8 — OIDC / JWT

Status: Done

Goal:

- Add OIDC support.
- Add JWT support.
- Add JWK configuration.
- Define basic token claims.

### Phase 9 — Scope-Based API Authorization

Status: Done

Goal:

- Protect management and SCIM APIs with JWT scopes.
- Require `iam.read` for read operations.
- Require `iam.write` for write operations.

### Phase 10 — Audit Logging

Status: Done

Goal:

- Record important security and administration events.
- Support future troubleshooting and compliance-style explanations.

### Phase 11 — MFA

Status: Done

Goal:

- Add TOTP-based multi-factor authentication.
- Handle MFA enrollment and verification.
- Protect MFA secrets carefully.

### Phase 11.5 — MFA Hardening

Status: Done

Goal:

- Add RFC 6238-compatible TOTP test coverage.
- Use constant-time TOTP verification comparison.
- Verify normal user responses do not expose MFA secrets.

### Phase 11.6 — Secret Protection Foundation

Status: Done

Goal:

- Add minimal encryption for stored MFA secrets.
- Keep local development key configuration simple.
- Preserve the existing MFA use cases without new APIs or schema changes.

### Phase 12 — SCIM Foundation

Status: Done

Goal:

- Add basic SCIM-style identity provisioning APIs.
- Support users.
- Add group foundations.

### Phase 12.5 — Group Membership Consistency

Status: Done

Goal:

- Validate tenant boundaries when removing users from groups.
- Avoid misleading audit events for no-op group removals.

### Phase 13 — OpenAPI Documentation

Status: Done

Goal:

- Add OpenAPI documentation.
- Provide API contracts.
- Improve project readability for developers and interviewers.

### Phase 14 — GitLab CI/CD

Status: Done

Goal:

- Add GitLab CI pipeline.
- Run tests automatically.
- Build application package.
- Build Docker image.

### Phase 14.5 — GitHub and GitLab Workflow Documentation

Status: Done

Goal:

- Document GitHub as the public portfolio repository.
- Document GitLab as the CI/CD practice repository.
- Explain the current dual-push workflow.

### Phase 15 — Architecture, Security, and Interview Documentation

Status: Done

Goal:

- Add architecture documentation.
- Add security design documentation.
- Add interview notes.
- Improve project readability for portfolio and interview review.

### Phase 16 — Documentation Cleanup After Foundation Stage

Status: Done

Goal:

- Correct release-like wording.
- Keep the project described as an IAM backend foundation prototype.
- Move detailed phase tracking out of `PROJECT_VISION.md`.
- Keep root documentation concise and accurate.

## Next Phase

### Phase 17 — User Authentication and Login Foundation

Status: Next

Goal:

- Add a foundation for user authentication and login behavior.
- Keep the scope small and explainable.
- Avoid turning the project into a production IAM product too early.

## Future Improvements

Future work may include:

- User authentication and login foundation.
- Account lifecycle flows.
- Password handling and password policy design.
- Stronger client secret handling.
- Token lifecycle improvements.
- Expanded SCIM compatibility.
- Docker image publishing to GitLab Container Registry.
- Deployment pipeline practice.
- CI/CD secret management.
- Operational documentation and runbook-style notes.

These items are backlog candidates, not part of a formal release.
