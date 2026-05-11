# Release Notes: v0.1.0

## Summary

`v0.1.0` is the first portfolio-ready MVP of `international-iam-platform`. It presents a Spring Boot IAM backend with production-inspired architecture, security concepts, persistence testing, API documentation, and CI/CD practice.

The release is designed for portfolio review and interview discussion. It is not intended to be used as a production IAM system.

## What This Release Contains

- Core IAM domain model for tenants, users, clients, roles, permissions, groups, and audit logs.
- Flyway-managed database schema changes.
- Spring Data JPA repositories and PostgreSQL Testcontainers persistence tests.
- Application services for core IAM workflows and tenant boundary validation.
- REST APIs with DTOs, validation, and centralized error handling.
- OAuth2 Authorization Server foundation.
- JWT / JWK support.
- Scope-based API authorization for management and SCIM APIs.
- Audit logging for important IAM and administration events.
- TOTP-based MFA enrollment and verification.
- Encryption foundation for stored MFA secrets.
- SCIM-style user and group provisioning foundation.
- OpenAPI documentation through Swagger UI.
- GitLab CI/CD pipeline for test, package, and Docker image build stages.
- Architecture, security design, and interview preparation documentation.

## What Can Be Demonstrated

- Running local dependencies with Docker Compose.
- Running the full test suite with Maven.
- Explaining the modular monolith architecture.
- Reviewing the request flow from controller to DTO, application service, repository, and entity.
- Discussing tenant-aware IAM modeling.
- Demonstrating API documentation in Swagger UI.
- Explaining OAuth2, JWT, JWK, and scope-based API authorization concepts.
- Discussing audit logging, MFA, MFA secret protection, and SCIM foundations.
- Showing GitLab CI/CD practice for automated test, package, and Docker build stages.

## Not Production-Grade Yet

This release intentionally does not include:

- Production-ready login and account lifecycle flows.
- Enterprise-grade key rotation or external secret management.
- Production deployment automation.
- Docker image publishing to a registry.
- Full observability, alerting, or operational runbooks.
- Complete enterprise SCIM compatibility.
- Production hardening for all IAM edge cases.

## Planned Future Improvements

- Prepare a clearer repository release package and tag.
- Add Docker image publishing to GitLab Container Registry.
- Add a deployment pipeline when the project is ready for deployment practice.
- Improve CI/CD secret management.
- Expand account lifecycle and authentication flows.
- Improve SCIM compatibility and operational documentation.
