# international-iam-platform

`international-iam-platform` is an IAM backend foundation prototype built with
Java 21 and Spring Boot.

The project is intended to explore and document the backend foundations of an
identity and access management system. It is useful for code review,
architecture discussion, continued development planning, and future AI-assisted
context recovery.

The project is not production-ready. It is not a complete usable IAM product
yet, and it does not currently include a frontend.

## Current Status

The current implementation includes:

- Core IAM domain model for tenants, users, clients, roles, permissions,
  groups, and audit logs.
- Flyway-managed PostgreSQL database migrations.
- Spring Data JPA repositories and PostgreSQL Testcontainers persistence tests.
- Application service layer for use-case orchestration and tenant boundary
  validation.
- REST API layer with DTOs, validation, centralized error handling, and
  OpenAPI documentation.
- OAuth2 Authorization Server foundation.
- Minimal server-side login flow for local platform users.
- JWT and JWK support.
- Scope-based API authorization using `iam.read` and `iam.write`.
- Audit logging for important IAM and administration events.
- TOTP MFA enrollment and verification.
- Encryption foundation for stored MFA secrets.
- SCIM-style user and group provisioning foundation.
- GitLab CI/CD pipeline for test, package, and Docker image build stages.

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

The health endpoint is public. Management APIs under `/api/**` and SCIM APIs
under `/scim/v2/**` require OAuth2 JWT scopes:

- Read operations require `iam.read`.
- Write operations require `iam.write`.

Local platform users with active password credentials can authenticate through
the minimal server-side `/login` form handled by Spring Security. This login
creates a normal authenticated session for browser-based Authorization Server
readiness. It does not grant access to management or SCIM APIs, which remain
protected by JWT scope checks.

## Roadmap

Current progress, the next development direction, and future planned work are
tracked in [docs/ROADMAP.md](docs/ROADMAP.md).

## Documentation

- [Project Vision](docs/PROJECT_VISION.md)
- [Roadmap](docs/ROADMAP.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Security Design](docs/SECURITY_DESIGN.md)
