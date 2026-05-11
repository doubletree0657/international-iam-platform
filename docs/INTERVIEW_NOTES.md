# Interview Notes

## Short Introduction

`international-iam-platform` is a Java 21 and Spring Boot IAM backend foundation prototype.

It demonstrates backend architecture, IAM domain modeling, PostgreSQL persistence, REST APIs, OAuth2/JWT foundations, scope-based API authorization, audit logging, TOTP MFA, SCIM-style provisioning foundations, OpenAPI documentation, Docker-based local development, and GitLab CI/CD practice.

It is production-inspired, but it is not a complete production-ready IAM product.

## One-Minute Explanation

I built this project as a portfolio IAM backend foundation. The goal was to show realistic identity and access management backend concepts without pretending the project is a finished enterprise product.

The architecture is a modular monolith. A typical request goes from controller to DTO, application service, repository, and entity. I chose this structure because it keeps the project easy to run and explain while still separating domain, persistence, web, security, audit, MFA, and SCIM responsibilities.

The current foundation includes OAuth2 Authorization Server support, JWT/JWK support, scope-based API authorization, tenant boundary validation, audit logging, TOTP MFA with encrypted MFA secrets, SCIM-style user and group provisioning foundations, OpenAPI documentation, and GitLab CI/CD.

## Technical Highlights

- Java 21 and Spring Boot 3.5.
- Modular monolith structure.
- PostgreSQL with Flyway migrations.
- Spring Data JPA repositories.
- PostgreSQL Testcontainers persistence tests.
- REST APIs with DTOs, validation, and centralized error handling.
- OpenAPI documentation through Swagger UI.
- Docker Compose for local PostgreSQL and Redis.
- GitLab CI/CD for test, package, and Docker image build stages.

## IAM And Security Highlights

- Core IAM model for tenants, users, clients, roles, permissions, groups, and audit logs.
- OAuth2 Authorization Server foundation.
- JWT and JWK support.
- Scope-based API authorization with `iam.read` and `iam.write`.
- Tenant boundary validation in application services.
- Audit logging for important IAM and administration events.
- TOTP MFA enrollment and verification.
- Encryption foundation for stored MFA secrets.
- SCIM-style provisioning foundation for users and groups.

## Design Decisions To Explain

### Why Modular Monolith

The project is a modular monolith because the current goal is to demonstrate clean backend boundaries, not distributed-system operations. It avoids premature microservice complexity while keeping the code organized by responsibility.

### Why Application Services

Application services hold use-case logic such as tenant validation, workflow coordination, audit event creation, and MFA behavior. This keeps important business rules outside controllers and close to the state changes they protect.

### Why Testcontainers

Persistence tests use PostgreSQL Testcontainers so JPA mappings and database behavior are tested against a realistic database instead of only an in-memory substitute.

### Why Scope-Based Authorization

The project uses `iam.read` and `iam.write` scopes to demonstrate OAuth2-style API protection. This is intentionally coarse-grained and can evolve into more detailed authorization policy later.

## Tradeoffs And Limitations

- The project is a prototype, not a production IAM platform.
- User authentication and account lifecycle flows are still limited.
- Secret and key management are local-development focused.
- SCIM support is foundational, not complete enterprise SCIM.
- CI/CD builds and tests the project but does not deploy it.
- Operational hardening, observability, and runbooks are future work.

## Possible Interview Questions

### How would you make this production-ready?

I would start with stronger key and secret management, production login and account lifecycle flows, password policy design, token lifecycle controls, more detailed authorization rules, observability, audit retention policy, deployment hardening, CI/CD secret management, and operational runbooks.

### Why not microservices?

The project does not need independent deployment or scaling boundaries yet. A modular monolith is simpler to run and test while still demonstrating clean internal separation.

### Where is tenant isolation enforced?

Tenant consistency checks are handled in application services for workflows that can cross tenant boundaries, such as role assignment and group membership behavior.

### What does the CI/CD pipeline prove?

It proves the project can run tests, package the application, and build a Docker image in GitLab CI. It does not yet publish images or deploy to an environment.

## Speaking Practice

- This project is an IAM backend foundation prototype, not a finished IAM product.
- I used Java 21 and Spring Boot because they are common choices for enterprise backend systems.
- The architecture is a modular monolith with clear internal boundaries.
- The persistence layer uses PostgreSQL, Flyway, and Testcontainers.
- Security foundations include OAuth2, JWT, JWK, scope-based authorization, audit logging, and TOTP MFA.
- Tenant boundary validation belongs in application services because it is part of the business workflow.
- MFA secrets are encrypted before persistence, but production key management is still future work.
- GitHub is used for portfolio presentation, and GitLab is used for CI/CD practice.
