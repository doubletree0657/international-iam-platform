# Roadmap

## Current Status

The IAM backend foundation stage is complete.

The project now has a working backend foundation for discussing IAM domain design, persistence, application services, REST APIs, OAuth2/JWT concepts, scope-based authorization, audit logging, MFA, SCIM foundations, OpenAPI documentation, and CI/CD practice.

This is a prototype milestone, not a formal `v0.1.0` release.

## Completed Foundation Work

### Project Baseline

Status: Done

- Spring Boot project created.
- Maven Wrapper added.
- Docker Compose added for PostgreSQL and Redis.
- Health check endpoint added.
- Local development baseline verified.

### Core IAM Domain And Persistence

Status: Done

- Core entities added for tenants, users, clients, roles, permissions, groups, and audit logs.
- Flyway migrations introduced.
- Spring Data JPA repositories added.
- PostgreSQL Testcontainers persistence tests added.

### Application Service Layer

Status: Done

- Use-case-oriented services added.
- Tenant boundary validation added for role assignment and group membership behavior.
- Audit event creation integrated into important workflows.

### REST API Layer

Status: Done

- REST controllers added for core IAM workflows.
- DTOs and validation added.
- Centralized error handling added.
- OpenAPI documentation added through Swagger UI.

### OAuth2, JWT, And API Authorization

Status: Done

- Spring Authorization Server foundation added.
- JWT and JWK support added.
- API authorization added with `iam.read` and `iam.write` scopes.
- Health endpoint remains public for local checks.

### Audit, MFA, And Secret Protection

Status: Done

- Audit logging added for important IAM and administration events.
- TOTP MFA enrollment and verification added.
- MFA verification hardening and test coverage added.
- Stored MFA secret encryption foundation added.

### SCIM Foundation

Status: Done

- SCIM-style user provisioning foundation added.
- SCIM-style group provisioning foundation added.
- Tenant consistency checks improved for group membership operations.

### CI/CD And Documentation

Status: Done

- GitLab CI pipeline added for test, package, and Docker image build stages.
- GitHub/GitLab workflow documented.
- Architecture, security design, roadmap, project vision, and interview notes added.
- Historical foundation-stage notes moved under `docs/archive/`.

## Next Phase

### User Authentication And Login Foundation

Status: Next

Goal:

- Add a focused foundation for user authentication and login behavior.
- Keep the scope small and explainable.
- Avoid claiming complete IAM product readiness.

Candidate work:

- Define the login use case boundaries.
- Add password handling foundations if needed.
- Clarify how login integrates with the authorization server.
- Add tests for security-sensitive behavior.
- Update documentation after implementation.

## Future Backlog

Future work may include:

- Account lifecycle flows.
- Password policy design.
- Stronger client secret handling.
- Token lifecycle improvements.
- Expanded SCIM compatibility.
- External secret management and key rotation design.
- Docker image publishing to GitLab Container Registry.
- Deployment pipeline practice.
- CI/CD secret management.
- Observability and operational runbook notes.

These items are backlog candidates, not committed release scope.

## Historical Notes

Historical foundation-stage summaries live under:

- [Foundation Stage Summary](archive/foundation-stage/FOUNDATION_SUMMARY.md)
- [Foundation Changelog](archive/foundation-stage/FOUNDATION_CHANGELOG.md)
