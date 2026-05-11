# Project Vision

## Purpose

`international-iam-platform` is a public portfolio project for building, explaining, and evolving an IAM backend foundation.

The project supports four goals:

- Future development planning.
- Portfolio presentation.
- Interview preparation.
- Clear context for AI-assisted development.

The current state is an IAM backend foundation prototype. It is production-inspired, but it is not a complete production-ready IAM product and it is not a formal release.

## Product Direction

The long-term direction is an explainable IAM backend that can demonstrate realistic identity and access management concerns:

- Multi-tenant identity modeling.
- OAuth2 and OIDC concepts.
- JWT-based API protection.
- Role, permission, group, and client management.
- Auditability for important administration and security events.
- MFA and secret protection foundations.
- SCIM-style provisioning concepts.
- API documentation and CI/CD practice.

The project should remain honest about its maturity. Each phase should add a focused backend capability without implying production readiness before the required operational, security, and product hardening exists.

## Engineering Goals

The project is intended to show:

- Modern Java and Spring Boot backend development.
- Modular monolith architecture.
- Clear domain modeling before product expansion.
- Use-case-oriented application services.
- Database migration discipline with Flyway.
- Persistence testing with PostgreSQL Testcontainers.
- Security design tradeoffs that can be explained in interviews.
- Practical local development with Docker Compose.
- CI/CD familiarity through GitLab pipelines.
- Clear technical communication in English.

## Architecture Direction

The project follows a modular monolith direction:

- One Spring Boot application.
- Clear internal responsibility boundaries.
- Simple local development and testing.
- No early distributed-system complexity.

This is intentional. At the current stage, internal boundaries matter more than service boundaries. A modular monolith keeps the project easier to run, review, and explain while still supporting future extraction if a real product need appears.

## Development Principles

- Keep the project explainable.
- Prefer small, focused phases.
- Keep documentation synchronized with the current code.
- Avoid over-engineering.
- Test persistence, tenant boundaries, and security-sensitive behavior.
- Treat AI-generated code as draft work that needs review.
- Clearly separate prototype behavior from production-ready claims.

## Security Principles

- Do not store passwords, client secrets, tokens, or MFA secrets in plain text.
- Do not log tokens, passwords, client secrets, or MFA secrets.
- Keep tenant boundary checks explicit and testable.
- Protect signing keys and encryption secrets.
- Do not commit real CI/CD secrets.
- Distinguish local development security from production-grade security.

## Non-Goals At This Stage

The project is not currently trying to be:

- A complete production IAM product.
- A formal product release.
- A full enterprise IAM replacement.
- A microservices platform.
- A Kubernetes or cloud infrastructure project.
- A clone of any company system.

## Target Outcome

The project should be easy to run, easy to review, and easy to discuss. It should help demonstrate backend engineering judgment, IAM domain understanding, security tradeoff awareness, and the ability to communicate architecture clearly.
