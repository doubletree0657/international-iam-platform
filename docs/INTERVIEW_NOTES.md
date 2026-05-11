# Interview Notes

## Short Introduction

`international-iam-platform` is a Spring Boot IAM portfolio project. It demonstrates backend design, OAuth2 concepts, tenant-aware access control, audit logging, MFA, persistence testing, OpenAPI documentation, Docker-based local development, and GitLab CI/CD practice.

## Architecture Explanation

The project follows a modular monolith direction. It runs as one application, but the code is separated into domain entities, repositories, application services, web controllers, authorization configuration, audit, MFA, and SCIM areas.

The main flow is:

```text
Controller -> DTO -> Application Service -> Repository -> Entity
```

This structure keeps the project easy to run locally while still showing clear backend boundaries.

## Key Technical Highlights

- Java 21 and Spring Boot 3.5.
- Spring Data JPA with PostgreSQL.
- Flyway database migrations.
- Persistence tests with PostgreSQL Testcontainers.
- REST APIs with DTOs and validation.
- OpenAPI documentation through Swagger UI.
- Docker Compose for local dependencies.
- GitLab CI/CD stages for test, package, and Docker image build.

## IAM And Security Highlights

- OAuth2 Authorization Server foundation.
- JWT and JWK support.
- Scope-based API authorization with `iam.read` and `iam.write`.
- Tenant boundary validation in application services.
- Audit logging for important IAM events.
- TOTP-based MFA.
- Encryption for stored MFA secrets.
- SCIM-style provisioning foundation for users and groups.

## Tradeoffs And Limitations

- The project is a modular monolith, not microservices.
- It is production-inspired, but not production-ready.
- Secret management is simple for local development.
- CI/CD builds and tests the project, but does not deploy it.
- SCIM and authentication flows are intentionally limited.

## Possible Future Improvements

- Release preparation and cleaner portfolio presentation.
- Docker image publishing to GitLab Container Registry.
- Deployment pipeline.
- Stronger CI/CD secret management.
- More complete account lifecycle and authentication flows.
- Expanded SCIM compatibility.

## Interview Speaking Practice

- This project is a portfolio IAM backend built with Java 21 and Spring Boot.
- I chose a modular monolith because it keeps the system simple while still showing clear internal boundaries.
- The main request flow is Controller, DTO, Application Service, Repository, and Entity.
- The project uses PostgreSQL, Flyway, and Testcontainers to make persistence behavior realistic and testable.
- Security is built around OAuth2 concepts, JWT access tokens, and scope-based API authorization.
- Tenant boundary validation is handled in the application service layer because it is part of the business use case.
- The MFA feature uses TOTP and encrypts stored MFA secrets.
- Audit logging records important IAM events for traceability.
- GitHub is used for portfolio presentation, and GitLab is used for CI/CD practice.
- The project is not production-ready yet, but it is designed to explain realistic backend and security tradeoffs.
