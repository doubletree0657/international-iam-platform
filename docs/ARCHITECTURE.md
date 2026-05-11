# Architecture

## Purpose

`international-iam-platform` is a portfolio IAM backend built with Spring Boot. It demonstrates identity and access management concepts, backend architecture, testing, security design, and CI/CD practice in a project that can be explained clearly in interviews.

The project is not a production IAM product. It is a learning and portfolio system with production-inspired boundaries.

## Architecture Style

The project follows a modular monolith direction.

It runs as one Spring Boot application, but the code is organized around clear responsibilities. This keeps local development simple while still showing how a larger backend can separate domain logic, application workflows, web APIs, persistence, and security concerns.

## Main Request Flow

Typical API requests follow this structure:

```text
Controller -> DTO -> Application Service -> Repository -> Entity
```

- Controllers expose REST and SCIM endpoints.
- DTOs define request and response shapes.
- Application services hold use-case logic and validation.
- Repositories provide persistence access through Spring Data JPA.
- Entities model IAM concepts such as tenants, users, clients, roles, permissions, groups, and audit logs.

## Key Modules

- `domain`: JPA entities and core IAM relationships.
- `repository`: Spring Data repositories for persistence.
- `application service`: use-case orchestration, tenant checks, audit events, and MFA workflows.
- `web`: REST controllers, DTOs, error handling, and OpenAPI configuration.
- `authorization`: OAuth2 Authorization Server and JWT/JWK configuration.
- `audit`: audit log model, repository, and application service.
- `mfa`: TOTP enrollment, verification, and MFA secret protection.
- `scim`: SCIM-style user and group provisioning foundation.

## Why Not Microservices

The project is intentionally not split into microservices.

A modular monolith is a better fit for this stage because it keeps the system easy to run, test, and explain. It avoids early distributed-system complexity such as service discovery, network failures, cross-service transactions, and duplicated deployment concerns.

The goal is to demonstrate strong module boundaries before introducing service boundaries.

## Current Limitations

- The project is not production hardened.
- User authentication flows are intentionally limited.
- Secret and key management is local-development focused.
- SCIM support is a foundation, not a complete enterprise SCIM implementation.
- CI/CD builds the application and Docker image, but does not deploy it.

## Future Evolution

Future phases may improve release readiness, deployment automation, container registry usage, stronger secret management, more complete IAM flows, and clearer operational documentation.
