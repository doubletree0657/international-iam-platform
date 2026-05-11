# Foundation Changelog

No formal release has been published yet.

## Foundation Stage - Completed

The IAM backend foundation stage is complete. This is a prototype milestone, not a formal `v0.1.0` release.

Included in the foundation stage:

- Project baseline with Spring Boot, Maven Wrapper, Docker Compose, PostgreSQL, and Redis.
- Core IAM domain model for tenants, users, clients, roles, permissions, groups, and audit logs.
- Persistence layer with Spring Data JPA, Flyway migrations, and PostgreSQL Testcontainers coverage.
- Application service layer for core IAM use cases and tenant boundary validation.
- REST API layer with DTOs, validation, error handling, and OpenAPI documentation.
- OAuth2 Authorization Server foundation.
- JWT / JWK support.
- Scope-based API authorization with `iam.read` and `iam.write`.
- Audit logging for important IAM and administration events.
- TOTP MFA enrollment and verification.
- MFA secret encryption foundation.
- SCIM-style provisioning foundation for users and groups.
- GitLab CI/CD pipeline with test, package, and Docker image build stages.
- Architecture, security design, roadmap, and interview documentation.
