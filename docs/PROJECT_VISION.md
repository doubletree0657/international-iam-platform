# International IAM Platform — Project Vision

## 1. Overview

`international-iam-platform` is a public portfolio project that implements a modern IAM (Identity and Access Management) platform.

The project is designed for learning, engineering practice, and international job preparation.

It is not a copy of any company system and does not use proprietary source code, internal documents, customer configurations, or real production data.

The goal is to build a clean, understandable, and production-inspired IAM system that can be explained clearly in interviews.

---

## 2. Project Goals

This project is built to demonstrate and improve:

- Modern Java backend development
- Spring Boot 3 application design
- IAM domain modeling
- Multi-tenant architecture
- Authentication and authorization concepts
- OAuth2 / OIDC understanding
- RBAC design
- Database migration and persistence testing
- Docker-based local development
- GitLab CI/CD practice
- Technical English communication

---

## 3. Architecture

The project follows a **modular monolith** architecture.

It is not designed as microservices at the current stage.

A modular monolith means:

- one Spring Boot application
- clear internal module boundaries
- simple local development
- easier testing and explanation
- no early distributed-system complexity

Target logical modules:

- `application`
- `authentication`
- `authorization`
- `tenant`
- `mfa`
- `scim`
- `audit`
- `common`

The project may start with a simpler package structure and gradually evolve toward these modules.

---

## 4. Technology Stack

Core stack:

- Java 21
- Spring Boot 3.5.x
- Maven Wrapper
- PostgreSQL
- Redis
- Flyway
- Docker Compose
- JUnit 5
- Testcontainers

Planned security stack:

- Spring Security
- Spring Authorization Server
- OAuth2
- OIDC
- JWT

Project platforms:

- GitHub for public portfolio display
- GitLab for CI/CD practice

---

## 5. Current Status

Current completed work:

- Project baseline
- Docker Compose setup
- Health check endpoint
- Core IAM domain model
- Flyway database migration
- Tenant-scoped role design
- Spring Data JPA repositories
- Persistence tests with PostgreSQL Testcontainers

Current phase:

- **Phase 6.5 — Tenant Boundary Validation: Done**

Next phase:

- **Phase 7 — OAuth2 Authorization Server**

---

## 6. MVP Roadmap

### Phase 1 — Project Baseline

Status: Done

Goal:

- Create the initial Spring Boot project
- Add README
- Add Docker Compose
- Add health check endpoint
- Verify local development environment

---

### Phase 2 — Core Domain Model

Status: Done

Goal:

- Define the core IAM entities
- Establish the basic identity and access model
- Keep the model simple and explainable

Core entities:

- Tenant
- User
- Client
- Role
- Permission

---

### Phase 3 — Database Migration

Status: Done

Goal:

- Introduce Flyway
- Create the initial database schema
- Keep database changes version-controlled

---

### Phase 4 — Repository Layer and Persistence Tests

Status: Done

Goal:

- Add Spring Data JPA repositories
- Verify entity mappings with PostgreSQL
- Use Testcontainers for realistic persistence tests

---

### Phase 5 — Application Service Boundary / Use Case Layer

Status: Done

Goal:

- Add minimal application services
- Define clear use case boundaries
- Prepare for future REST APIs and security integration

Example use cases:

- Create tenant
- Create user
- Create role
- Assign role to user
- Create permission
- Assign permission to role
- Create client

This phase should not implement authentication, OAuth2, RBAC enforcement, MFA, or SCIM yet.

---

### Phase 6 — REST API Layer

Status: Done

Goal:

- Add basic REST APIs
- Add DTOs
- Add validation
- Add basic error handling
- Keep APIs unauthenticated at first for development simplicity

---

### Phase 6.5 — Tenant Boundary Validation

Status: Done

Goal:

- Prevent cross-tenant role assignment
- Keep tenant boundary checks in the application service layer
- Prepare safer domain behavior before OAuth2 work

---

### Phase 7 — OAuth2 Authorization Server

Status: Next

Goal:

- Introduce Spring Authorization Server
- Support registered clients
- Prepare token issuing capability
- Build the foundation for OAuth2 flows

---

### Phase 8 — OIDC / JWT

Status: Planned

Goal:

- Add OIDC support
- Add JWT support
- Add JWK configuration
- Define basic token claims

---

### Phase 9 — RBAC Authorization

Status: Planned

Goal:

- Implement role-based access control
- Evaluate permissions through user-role-permission relationships
- Protect selected APIs with authorization rules

---

### Phase 10 — Audit Logging

Status: Planned

Goal:

- Record important security and administration events
- Support future troubleshooting and compliance-style explanations

Example audit events:

- user created
- role assigned
- permission changed
- client created
- authentication event

---

### Phase 11 — MFA

Status: Planned

Goal:

- Add TOTP-based multi-factor authentication
- Handle MFA enrollment and verification
- Protect MFA secrets carefully

---

### Phase 12 — SCIM

Status: Planned

Goal:

- Add basic SCIM-style identity provisioning APIs
- Support Users
- Support Groups if needed

Group support is intentionally postponed until this stage or a later authorization-model expansion stage.

---

### Phase 13 — OpenAPI Documentation

Status: Planned

Goal:

- Add OpenAPI documentation
- Provide API contracts
- Improve project readability for developers and interviewers

---

### Phase 14 — GitLab CI/CD

Status: Planned

Goal:

- Add GitLab CI pipeline
- Run tests automatically
- Build application package
- Build Docker image

---

## 7. Core Domain Design

The current access model is:

- Tenant owns Users
- Tenant owns Roles
- Tenant owns Clients
- User has Roles
- Role has Permissions

Design decisions:

- A user belongs to a tenant
- A role belongs to a tenant
- A client belongs to a tenant
- Permissions are global for now
- A user can have multiple roles
- A role can have multiple permissions

Group is not implemented yet.

Possible future model:

- User -> Group -> Role -> Permission
- User -> Role -> Permission

Group support is postponed to keep the MVP simple.

---

## 8. Development Principles

The project should follow these principles:

- Start simple and evolve gradually
- Avoid over-engineering
- Do not introduce security complexity too early
- Design the domain model before implementing features
- Verify important assumptions with tests
- Keep the code explainable
- Keep documentation synchronized with progress
- Review AI-generated code before committing

---

## 9. Security Principles

Security-sensitive features must be introduced carefully.

Important rules:

- Do not store passwords in plain text
- Do not store client secrets in plain text
- Do not log tokens, passwords, client secrets, or MFA secrets
- Protect signing keys and secrets
- Do not commit CI/CD secrets
- Do not use real company or customer data

---

## 10. AI Development Rules

This project uses AI-assisted development.

Tool responsibilities:

- ChatGPT: planning, architecture guidance, task breakdown, Codex prompts, learning rhythm
- Codex: implementation of clear and limited tasks
- Cursor: code explanation and learning support
- GitHub: public portfolio display
- GitLab: CI/CD practice

Rules for Codex:

- Follow this document before making changes
- Work only on the current phase
- Do not rewrite the project from scratch
- Do not introduce unrelated technologies
- Do not jump ahead to OAuth2, MFA, SCIM, Kubernetes, or cloud infrastructure
- After completing a phase, update the roadmap status in this document

---

## 11. Non-Goals

The project is currently not trying to be:

- a production-ready IAM system
- a full enterprise IAM replacement
- a microservices system
- a Kubernetes project
- a cloud infrastructure project
- a clone of any existing company product

---

## 12. Target Outcome

The final project should be:

- easy to run locally
- easy to understand
- easy to explain in interviews
- structured like a real backend project
- useful for discussing IAM design
- useful for discussing backend architecture
- useful for discussing security tradeoffs
- suitable as an international portfolio project
