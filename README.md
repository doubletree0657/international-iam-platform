# international-iam-platform

`international-iam-platform` is a public portfolio project for a Spring Boot based identity and access management platform. Phase 1 establishes a clean baseline only: project structure, local dependencies, documentation, and a simple health check.

IAM business features are intentionally out of scope for this phase. User, tenant, role, permission, OAuth2, MFA, and SCIM logic will be added later.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Data Redis
- Spring Modulith
- PostgreSQL
- Redis
- Maven Wrapper
- Docker Compose

## Local Development Environment

Required tools:

- JDK 21
- Docker or a Docker-compatible runtime
- Git

The project uses the Maven Wrapper, so a local Maven installation is not required.

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

## Roadmap

- Phase 1: Clean public baseline, documentation, local dependencies, and health check.
- Phase 2: Domain model and persistence boundaries for core IAM concepts.
- Phase 3: Authentication and authorization flows.
- Phase 4: Multi-tenancy, role, and permission management.
- Phase 5: OAuth2 authorization server hardening.
- Phase 6: MFA and account recovery flows.
- Phase 7: SCIM provisioning and operational readiness.
