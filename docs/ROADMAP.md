# Roadmap

## Current State

The project is currently an IAM backend foundation prototype.

It has a working backend foundation for IAM domain modeling, persistence,
application services, REST APIs, OAuth2/JWT concepts, scope-based API
authorization, audit logging, MFA, SCIM-style provisioning foundations, OpenAPI
documentation, and CI/CD practice.

It is not production-ready, not a complete usable IAM product, and does not yet
include user login flows or a frontend.

This roadmap is for continued development planning. It is not a release record.

## Implemented Foundations

### Project Baseline

- Spring Boot project baseline.
- Maven Wrapper.
- Docker Compose for PostgreSQL and Redis.
- Health check endpoint.
- Local development workflow.

### Core IAM Domain Model

- Tenant.
- User.
- Client.
- Role.
- Permission.
- Group.
- Group membership.
- Audit log.

### Persistence

- Flyway-managed database migrations.
- Spring Data JPA repositories.
- PostgreSQL persistence tests with Testcontainers.

### Application Services

- Use-case-oriented service layer.
- Tenant boundary validation for selected workflows.
- Service-level orchestration for core IAM operations.

### REST API Layer

- REST controllers for core IAM management workflows.
- DTO-based request and response models.
- Validation.
- Centralized error handling.
- OpenAPI documentation.

### OAuth2, JWT, And API Authorization

- Spring Authorization Server foundation.
- Registered client support.
- JWT support.
- JWK support.
- Scope-based API authorization using `iam.read` and `iam.write`.

### Audit Logging

- Audit log model and persistence.
- Audit events for important IAM and administration operations.
- Foundation for future traceability and operational review.

### MFA And Secret Protection

- TOTP MFA enrollment and verification.
- TOTP verification hardening.
- Protection against exposing MFA secrets in normal responses.
- Encryption foundation for stored MFA secrets.

### SCIM Foundation

- SCIM-style user provisioning foundation.
- SCIM-style group provisioning foundation.
- Tenant consistency checks for group membership behavior.

### Documentation And CI/CD

- OpenAPI / Swagger UI documentation.
- GitLab CI pipeline for test, package, and Docker image build stages.
- Project vision, roadmap, architecture, and security design documentation.

## Next Development Direction

### User Authentication And Login Foundation

The next development direction is to add a focused foundation for user
authentication and login behavior.

The goal is to introduce login-related backend behavior without claiming that
the project has become a complete IAM product.

Candidate work:

- Define login use case boundaries.
- Decide how login integrates with Spring Authorization Server.
- Add password handling foundations if needed.
- Add account state concepts only if required by the login workflow.
- Add tests for security-sensitive behavior.
- Update architecture and security documentation after implementation.

## Future Planned Work

Future development may include:

- Account lifecycle flows.
- Password policy design.
- Stronger client secret handling.
- Token lifecycle improvements.
- More detailed authorization policy.
- Expanded SCIM compatibility.
- External secret management and key rotation design.
- Docker image publishing to GitLab Container Registry.
- Deployment pipeline practice.
- CI/CD secret management.
- Observability and operational runbook-style documentation.

These items are planning candidates and may change as the project evolves.
