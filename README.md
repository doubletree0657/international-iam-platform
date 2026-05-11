# international-iam-platform

`international-iam-platform` is an IAM backend foundation prototype built with Spring Boot.

It demonstrates core identity and access management backend concepts, including domain modeling, persistence, REST APIs, OAuth2/JWT foundations, scope-based authorization, audit logging, MFA, SCIM-style provisioning foundations, OpenAPI documentation, and CI/CD practice.

The project is production-inspired, but it is not production-ready and is not a complete usable IAM product.

## Current Status

The backend foundation stage is complete. No formal release is being published yet.

Implemented capabilities include:

- Core IAM domain model for tenants, users, clients, roles, permissions, groups, and audit logs.
- Flyway-managed database migrations.
- Spring Data JPA repositories and PostgreSQL Testcontainers persistence tests.
- Application service layer with tenant boundary validation.
- REST API layer with DTOs, validation, and centralized error handling.
- OAuth2 Authorization Server foundation.
- JWT / JWK support.
- Scope-based API authorization with `iam.read` and `iam.write`.
- Audit logging for important IAM and administration events.
- TOTP MFA with MFA secret encryption.
- SCIM-style user and group provisioning foundation.
- OpenAPI documentation through Swagger UI.
- GitLab CI/CD pipeline for test, package, and local Docker image build stages.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Spring Data Redis
- Spring Modulith
- Spring Security
- Spring Authorization Server
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

The project uses the Maven Wrapper, so a local Maven installation is not required.

Start PostgreSQL and Redis:

```bash
docker compose up -d
```

Stop local dependencies:

```bash
docker compose down
```

Local services:

- PostgreSQL: `localhost:5432`
- Redis: `localhost:6379`

The Compose credentials are local development values only and must not be reused as production secrets.

## Run Tests

```bash
./mvnw test
```

Some tests use Testcontainers and require Docker access.

## Run the Application

After local dependencies are running:

```bash
./mvnw spring-boot:run
```

Health check:

```bash
curl http://localhost:8080/api/health
```

Expected response:

```json
{
  "status": "UP",
  "service": "international-iam-platform"
}
```

## API Documentation

Swagger UI is available after the application starts:

```text
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI JSON document is available at:

```text
http://localhost:8080/v3/api-docs
```

The health check is public. Management APIs under `/api/**` and SCIM APIs under `/scim/v2/**` require OAuth2 JWT scopes:

- Read operations require `iam.read`.
- Write operations require `iam.write`.

## GitHub and GitLab Workflow

GitHub is the main public portfolio repository. GitLab is used for CI/CD practice.

Current workflow:

- Push source changes to GitHub for portfolio display.
- Push the same branch to GitLab for CI/CD practice.
- Let the GitLab pipeline run after the GitLab push.

The GitLab pipeline currently runs test, package, and Docker image build stages. The Docker stage builds an image in CI but does not push it to a registry.

## Documentation

- [Project Vision](docs/PROJECT_VISION.md)
- [Roadmap](docs/ROADMAP.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Security Design](docs/SECURITY_DESIGN.md)
- [Interview Notes](docs/INTERVIEW_NOTES.md)
