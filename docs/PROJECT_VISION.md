# Project Vision

## Purpose

`international-iam-platform` is a project for building the backend foundations
of an identity and access management system.

The documentation should help future developers and AI tools quickly recover
project context, understand the current direction, and continue development
without treating the project as a finished IAM product.

The current project state is an IAM backend foundation prototype. It is not
production-ready, not a complete usable IAM product, and does not yet include
user login flows or a frontend.

Current progress and future development planning are tracked in the
[Roadmap](ROADMAP.md).

## Project Direction

The project is moving toward an explainable IAM backend that demonstrates
realistic identity, authorization, provisioning, audit, and security concepts.

The direction is intentionally backend-first:

- Build a clear IAM domain model.
- Keep tenant boundaries explicit.
- Expose focused REST and SCIM-style APIs.
- Use OAuth2 and JWT concepts for API protection.
- Add security-sensitive features with tests and clear documentation.
- Keep operational and product maturity separate from prototype capability.

## Final Goal

The long-term goal is a coherent IAM backend that can support:

- Tenant-aware user, client, role, permission, and group management.
- User authentication and account lifecycle flows.
- OAuth2 and OIDC-style authorization behavior.
- Token lifecycle management.
- MFA and secret protection.
- SCIM-style provisioning.
- Auditability for important administration and security events.
- Clear operational documentation for local and deployment workflows.

The final goal is not to clone an existing IAM product. The goal is to build a
clear, maintainable backend that shows the major design concerns of an IAM
platform.

## Architecture Design

The project follows a modular monolith direction.

This means:

- One Spring Boot application.
- Clear internal responsibility boundaries.
- Simple local development and testing.
- No early distributed-system complexity.

At the current stage, internal module boundaries are more important than
microservice boundaries. The project should remain easy to run locally and easy
to reason about while still separating domain, application, web, persistence,
security, audit, MFA, and SCIM responsibilities.

## Technology Stack

Core stack:

- Java 21.
- Spring Boot 3.5.
- Maven Wrapper.
- PostgreSQL.
- Redis.
- Flyway.
- Docker Compose.
- JUnit 5.
- Testcontainers.

Security and API stack:

- Spring Security.
- Spring Authorization Server.
- OAuth2 foundations.
- JWT and JWK support.
- OpenAPI / Swagger UI.

Project platforms:

- GitHub for source hosting and project review.
- GitLab for CI/CD practice.

## Development Principles

- Keep the project explainable.
- Prefer small, focused development steps.
- Keep documentation synchronized with the current code.
- Avoid over-engineering before product requirements justify it.
- Put business-sensitive checks in application services where appropriate.
- Verify persistence, tenant boundaries, and security-sensitive behavior with
  tests.
- Treat AI-generated code as draft work that needs review.
- Record important context in documentation so future conversations can resume
  quickly.

## Security Principles

- Do not store passwords, client secrets, tokens, or MFA secrets in plain text.
- Do not log tokens, passwords, client secrets, or MFA secrets.
- Keep tenant boundary checks explicit and testable.
- Keep local development secrets separate from production secrets.
- Do not commit real CI/CD secrets.
- Clearly distinguish prototype security from production-grade security.

## Current Non-Goals

The project is not currently trying to be:

- A complete production IAM product.
- A complete user-facing application.
- A frontend application.
- A microservices platform.
- A Kubernetes or cloud infrastructure project.
- A clone of an existing commercial IAM product.
- A release preparation exercise.
