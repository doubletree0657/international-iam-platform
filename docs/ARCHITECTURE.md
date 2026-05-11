# Architecture

## Overview

`international-iam-platform` is a Spring Boot IAM backend foundation prototype.
It is structured to demonstrate realistic backend boundaries while remaining
small enough to run locally and reason about during continued development.

The system is not a production IAM product. It is a development prototype with
production-inspired architecture.

## Architecture Style

The project follows a modular monolith direction.

It runs as one Spring Boot application, with code organized around clear
responsibilities:

- Domain model.
- Application services.
- Persistence.
- Web APIs.
- Authorization server configuration.
- Audit logging.
- MFA.
- SCIM provisioning foundation.

This avoids early microservice complexity while still showing how backend
concerns can be separated.

## Request Flow

Typical management and SCIM requests follow this shape:

```text
Controller -> DTO -> Application Service -> Repository -> Entity
```

- Controllers expose HTTP endpoints.
- DTOs define request and response contracts.
- Application services coordinate use cases, validation, tenant checks, audit
  events, and MFA workflows.
- Repositories provide persistence access through Spring Data JPA.
- Entities model IAM concepts and relationships.

## Main System Areas

### Domain

The domain model includes tenants, users, clients, roles, permissions, groups,
group memberships, and audit logs. The model is intentionally foundational
rather than feature-complete.

### Persistence

PostgreSQL is the primary database. Flyway manages schema migrations, and
repository behavior is tested with PostgreSQL Testcontainers.

### Application Services

Application services hold use-case logic and protect important business
boundaries, including tenant consistency checks. This keeps sensitive rules
close to the workflows that depend on them.

### Web API

The REST API layer exposes core IAM management endpoints and SCIM-style
provisioning endpoints. It uses DTOs, validation, centralized error handling,
and OpenAPI documentation.

### Authorization

Spring Authorization Server provides the OAuth2 foundation. JWT and JWK support
are present for token-based API authorization.

### Audit

Audit logging records important IAM and administration events. The current
design supports traceability discussions but is not a complete compliance or
SIEM solution.

### MFA

The MFA area supports TOTP enrollment and verification, with stored MFA secret
encryption as a foundation for secret protection.

### SCIM

The SCIM area provides a foundation for user and group provisioning concepts. It
is not a complete enterprise SCIM implementation.

## Why A Modular Monolith

A modular monolith is the right fit for the current stage because it:

- Keeps local setup simple.
- Keeps tests easier to run.
- Makes the architecture easier to understand.
- Avoids premature distributed-system concerns.
- Still supports clear internal boundaries.

Microservices may be considered only if the project grows enough to justify
separate deployment, ownership, scaling, or operational boundaries.

## Current Limitations

- User authentication and account lifecycle flows are still limited.
- Secret and key management are local-development focused.
- SCIM support is foundational.
- The CI/CD pipeline builds and tests but does not deploy.
- Operational hardening, observability, alerting, and runbooks are future work.

## Evolution Path

The next architectural direction is a focused user authentication and login
foundation. Later work may add stronger token lifecycle handling, external
secret management, deployment practice, and operational documentation.
