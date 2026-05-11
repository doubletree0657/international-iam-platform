# International IAM Platform — Project Vision

## Overview

`international-iam-platform` is a public portfolio project for building and explaining an IAM (Identity and Access Management) backend foundation.

The project is designed for learning, engineering practice, and international job preparation. It is not a copy of any company system and does not use proprietary source code, internal documents, customer configurations, or real production data.

The current project state is an IAM backend foundation prototype. It is production-inspired, but it is not production-ready and is not a complete usable IAM product.

Detailed phase tracking lives in the [Roadmap](ROADMAP.md).

## Project Goals

This project is built to demonstrate and improve:

- Modern Java backend development.
- Spring Boot 3 application design.
- IAM domain modeling.
- Multi-tenant architecture.
- Authentication and authorization concepts.
- OAuth2 / OIDC understanding.
- Scope-based API authorization.
- Database migration and persistence testing.
- Docker-based local development.
- GitLab CI/CD practice.
- Technical English communication.

## Architecture Direction

The project follows a modular monolith direction.

A modular monolith means:

- One Spring Boot application.
- Clear internal module boundaries.
- Simple local development.
- Easier testing and explanation.
- No early distributed-system complexity.

The project is intentionally not designed as microservices at this stage. The goal is to keep the system explainable while still separating domain logic, application services, web APIs, persistence, security, audit, MFA, and SCIM responsibilities.

## Technology Stack

Core stack:

- Java 21.
- Spring Boot 3.5.x.
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
- OAuth2.
- OIDC / JWT foundations.
- JWK support.
- OpenAPI / Swagger UI.

Project platforms:

- GitHub for public portfolio display.
- GitLab for CI/CD practice.

## Development Principles

The project should follow these principles:

- Start simple and evolve gradually.
- Avoid over-engineering.
- Keep module boundaries understandable.
- Design the domain model before adding product features.
- Verify important assumptions with tests.
- Keep the code explainable.
- Keep documentation synchronized with the current project state.
- Review AI-generated code before committing.

## Security Principles

Security-sensitive features must be introduced carefully.

Important rules:

- Do not store passwords in plain text.
- Do not store client secrets in plain text.
- Do not log tokens, passwords, client secrets, or MFA secrets.
- Protect signing keys and secrets.
- Do not commit CI/CD secrets.
- Do not use real company or customer data.
- Clearly distinguish local-development security from production-grade security.

## Non-Goals

The project is currently not trying to be:

- A production-ready IAM system.
- A full enterprise IAM replacement.
- A microservices system.
- A Kubernetes project.
- A cloud infrastructure project.
- A clone of any existing company product.
- A formal product release.

## Target Outcome

The project should be:

- Easy to run locally.
- Easy to understand.
- Easy to explain in interviews.
- Structured like a real backend project.
- Useful for discussing IAM design.
- Useful for discussing backend architecture.
- Useful for discussing security tradeoffs.
- Suitable as an international portfolio project.
