# international-iam-platform

`international-iam-platform` is a Spring Boot IAM backend foundation prototype.

It demonstrates identity and access management backend concepts in a compact, interview-friendly codebase: domain modeling, persistence, REST APIs, OAuth2/JWT foundations, scope-based API authorization, audit logging, TOTP MFA, SCIM-style provisioning foundations, OpenAPI documentation, and GitLab CI/CD practice.

The project is production-inspired, but it is not a complete production-ready IAM product and it is not being presented as a formal `v0.1.0` release.

## Current Status

The IAM backend foundation stage is complete.

Implemented foundations include:

- Core IAM domain model for tenants, users, clients, roles, permissions, groups, and audit logs.
- Flyway-managed PostgreSQL schema migrations.
- Spring Data JPA repositories with PostgreSQL Testcontainers persistence tests.
- Application services with tenant boundary validation.
- REST API layer with DTOs, validation, centralized error handling, and OpenAPI documentation.
- OAuth2 Authorization Server foundation with JWT and JWK support.
- Scope-based API authorization using `iam.read` and `iam.write`.
- Audit logging for important IAM and administration events.
- TOTP MFA enrollment and verification.
- Encryption foundation for stored MFA secrets.
- SCIM-style user and group provisioning foundation.
- GitLab CI/CD pipeline for test, package, and local Docker image build stages.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Security and Spring Authorization Server
- Spring Data JPA
- PostgreSQL
- Redis
- Flyway
- Testcontainers
- Maven Wrapper
- Docker Compose
- OpenAPI / Swagger UI
- GitLab CI/CD

## Local Development

Required tools:

- JDK 21
- Docker or a Docker-compatible runtime
- Git

Start local dependencies:

```bash
docker compose up -d
```

Run tests:

```bash
./mvnw test
```

Run the application:

```bash
./mvnw spring-boot:run
```

Health check:

```bash
curl http://localhost:8080/api/health
```

Stop local dependencies:

```bash
docker compose down
```

Local services:

- PostgreSQL: `localhost:5432`
- Redis: `localhost:6379`

The Docker Compose credentials are local development values only.

## API Documentation

After the application starts:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

The health endpoint is public. Management APIs under `/api/**` and SCIM APIs under `/scim/v2/**` require OAuth2 JWT scopes:

- Read operations require `iam.read`.
- Write operations require `iam.write`.

## Repository Workflow

GitHub is the public portfolio repository. GitLab is used for CI/CD practice.

The current GitLab pipeline runs test, package, and Docker image build stages. The Docker stage builds an image in CI but does not publish it to a registry yet.

## Documentation

- [Project Vision](docs/PROJECT_VISION.md)
- [Roadmap](docs/ROADMAP.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Security Design](docs/SECURITY_DESIGN.md)
- [Interview Notes](docs/INTERVIEW_NOTES.md)
- [Foundation Stage Summary](docs/archive/foundation-stage/FOUNDATION_SUMMARY.md)
