# international-iam-platform

`international-iam-platform` is a public portfolio project for a Spring Boot based identity and access management platform. It demonstrates IAM domain modeling, REST APIs, OAuth2/JWT concepts, scope-based authorization, audit logging, MFA, SCIM foundations, persistence testing, and CI/CD practice.

The project is production-inspired, but it is not a production IAM system.

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

## Local Development Environment

Required tools:

- JDK 21
- Docker or a Docker-compatible runtime
- Git

The project uses the Maven Wrapper, so a local Maven installation is not required.

## Current Domain Model

The current model represents core IAM concepts with tenant-aware relationships.

- `Tenant`: an organization or customer boundary.
- `User`: an account principal that belongs to one tenant.
- `Client`: an OAuth2 client registration that belongs to one tenant.
- `Role`: a tenant-scoped access grouping assignable to users.
- `Permission`: a named capability assignable to roles.
- `Group`: a SCIM-style grouping concept for provisioning foundations.
- `AuditLog`: a record of important IAM and administration events.

Relationships:

- A `User` belongs to one `Tenant`.
- A `Client` belongs to one `Tenant`.
- A `Role` belongs to one `Tenant`.
- A `User` can have multiple `Role` entries.
- A `Role` can have multiple `Permission` entries.
- A `Group` belongs to one `Tenant` and can contain users.

All domain entities use UUID primary keys and basic timestamp metadata.

## Start Local Dependencies

Start PostgreSQL and Redis:

```bash
docker compose up -d
```

Stop them when finished:

```bash
docker compose down
```

Local Docker Compose services:

- PostgreSQL: `localhost:5432`
- Redis: `localhost:6379`

The Compose credentials are local development values only and must not be reused as production secrets.

## Run Tests

```bash
./mvnw test
```

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

The health check is public. Management APIs under `/api/**` and SCIM APIs under `/scim/v2/**` require OAuth2 JWT scopes: write operations require `iam.write`, and read operations require `iam.read`.

## Project Documentation

- [Project Vision](docs/PROJECT_VISION.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Security Design](docs/SECURITY_DESIGN.md)
- [Interview Notes](docs/INTERVIEW_NOTES.md)

## CI/CD Practice

GitHub is the main public portfolio repository. GitLab is used for CI/CD practice.

Current repository workflow:

- Push source changes to GitHub for portfolio display.
- Push the same branch to GitLab for CI/CD practice.
- Let the GitLab pipeline run after the GitLab push.

The GitLab pipeline is intentionally minimal:

- `test`: runs `./mvnw test` on JDK 21 with Docker-in-Docker for Testcontainers.
- `package`: runs `./mvnw package -DskipTests` and stores the built JAR as an artifact.
- `docker`: builds a local CI Docker image for the Spring Boot application without pushing it to a Docker registry.

Future CI/CD improvements may include:

- Configuring GitLab repository mirroring from GitHub if available.
- Pushing Docker images to the GitLab Container Registry.
- Adding a deployment pipeline.
- Managing CI/CD secrets through GitLab CI/CD variables or another secure secret-management approach.

## Roadmap

- Phase 1: Clean public baseline, documentation, local dependencies, and health check. Done.
- Phase 2: Domain model and persistence boundaries for core IAM concepts. In progress.
- Phase 3: Authentication and authorization flows.
- Phase 4: Multi-tenancy, role, and permission management.
- Phase 5: OAuth2 authorization server hardening.
- Phase 6: MFA and account recovery flows.
- Phase 7: SCIM provisioning and operational readiness.
