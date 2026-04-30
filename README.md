# international-iam-platform

`international-iam-platform` is a public portfolio project for a Spring Boot based identity and access management platform. The current baseline establishes project structure, local dependencies, documentation, a simple health check, and the first persistence model for core IAM concepts.

IAM business features are intentionally out of scope for the current phase. Authentication, login, OAuth2 flows, authorization checks, MFA, SCIM, service-layer workflows, and REST APIs for IAM entities will be added later.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Web
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

## Current Domain Model

The Phase 2 model defines JPA entities only. It does not implement business logic, controllers, DTOs, repositories, authentication, or authorization flows.

- `Tenant`: an organization or customer boundary.
- `User`: an account principal that belongs to one tenant.
- `Client`: an OAuth2 client registration shell that belongs to one tenant.
- `Role`: a tenant-scoped access grouping assignable to users.
- `Permission`: a named capability assignable to roles.

Relationships:

- A `User` belongs to one `Tenant`.
- A `Client` belongs to one `Tenant`.
- A `Role` belongs to one `Tenant`.
- A `User` can have multiple `Role` entries.
- A `Role` can have multiple `Permission` entries.

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

## Roadmap

- Phase 1: Clean public baseline, documentation, local dependencies, and health check. Done.
- Phase 2: Domain model and persistence boundaries for core IAM concepts. In progress.
- Phase 3: Authentication and authorization flows.
- Phase 4: Multi-tenancy, role, and permission management.
- Phase 5: OAuth2 authorization server hardening.
- Phase 6: MFA and account recovery flows.
- Phase 7: SCIM provisioning and operational readiness.
