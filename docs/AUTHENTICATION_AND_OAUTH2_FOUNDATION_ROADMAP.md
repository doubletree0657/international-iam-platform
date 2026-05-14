# Authentication and OAuth2 Foundation Roadmap

## Purpose

This roadmap defines the next medium-term development cycle for `international-iam-platform`.

The project is currently an IAM backend foundation prototype. It already has core IAM domain models, REST APIs, audit logging, MFA foundation, SCIM foundation, Spring Authorization Server baseline configuration, OpenAPI documentation, Docker Compose, and GitLab CI/CD.

However, it is not yet a usable IAM or IdP product because it does not have a complete identity authentication and authorization loop.

The goal of this roadmap is to move the project from:

> IAM backend foundation prototype

toward:

> A backend-first IAM / IdP foundation with local user authentication, persistent OAuth2 clients, and a minimal Authorization Code Flow readiness path.

This roadmap is intentionally medium-sized. It is larger than a single Codex task, but smaller than the full long-term product roadmap.

## Medium-Term Goal

By the end of this roadmap cycle, the platform should have a credible backend foundation for:

- Local user password credentials.
- Basic user account status.
- Password management APIs.
- Spring Security user authentication integration.
- Minimal backend login readiness.
- Persistent OAuth2 registered client modeling.
- Preparation for Authorization Code Flow.
- Clear documentation explaining what is implemented and what remains future work.

This roadmap does not aim to build a complete commercial IAM product yet. It focuses on the core identity loop required before building frontend management consoles, advanced MFA flows, enterprise federation, or full SCIM lifecycle features.

## Current Major Gaps

The current project still lacks:

- Password credential model.
- Account password authentication.
- Login flow connected to Spring Security.
- MFA integration into real login.
- Complete OAuth2 Authorization Code Flow.
- Persistent RegisteredClient integration.
- Client secret handling.
- Redirect URI and grant type configuration.
- Consent model.
- Admin UI.
- Production-grade session and token lifecycle management.

## Development Principles

This roadmap follows these principles:

1. Backend first.
2. Small, reviewable development tasks.
3. Security-sensitive changes must be explicit and testable.
4. Do not introduce frontend too early.
5. Do not implement full Authorization Code Flow before user authentication and client persistence are ready.
6. Do not connect MFA into login until the basic login flow is stable.
7. Do not expose secrets in DTOs, logs, audit logs, OpenAPI examples, or tests.
8. Keep all project-facing documentation, APIs, commit messages, and release notes in English.
9. Use Codex for small implementation tasks and Cursor for code understanding after each task.
10. Prefer MVP implementation over large speculative design.

---

# Phase 1: Password Credential and Account Status Foundation

## Goal

Turn the current `User` model from a directory-style identity record into an authenticatable local identity.

This phase introduces the minimum domain and persistence foundation required for password-based authentication.

## Scope

This phase should include:

- Password hash field.
- Password metadata.
- Basic account status.
- Database migration.
- Entity update.
- Safe DTO behavior.
- Persistence tests.
- Basic documentation update.

## Recommended Concepts

Password credential fields may include:

- `passwordHash`
- `passwordUpdatedAt`
- `passwordResetRequired`
- `credentialsVersion`

Account status may include:

- `ACTIVE`
- `DISABLED`
- `LOCKED`
- `PENDING`

## Completion Criteria

This phase is complete when:

- Users can store a password hash.
- Raw passwords are never stored.
- Password hashes are never returned by API responses.
- Basic account status exists.
- The default account status is clear.
- Flyway migration is added without modifying existing migrations.
- Persistence tests pass.
- Documentation states that password credential foundation has started.

## Progress Notes

- Password credential fields and account status foundation added.
- New users default to `PENDING` with password reset required because the current create-user flow does not collect or encode passwords yet.
- Login, password management API, password encoding, and MFA-in-login remain future work.

## Out of Scope

This phase does not include:

- Login endpoint.
- Spring Security authentication provider.
- MFA during login.
- OAuth2 Authorization Code Flow.
- Password reset by email.
- Account lockout policy.
- Frontend login page.

---

# Phase 2: Password Management Application Layer

## Goal

Add safe backend use cases for setting and updating user passwords.

This phase makes password credentials manageable through the application layer without directly exposing credential handling through controllers or repositories.

## Scope

This phase should include:

- Password encoding with Spring Security `PasswordEncoder`.
- Set initial password use case.
- Update password use case.
- Mark password reset required or completed.
- Audit logging for password-related events.
- Application service tests.
- Validation of password input.
- Safe error handling.

## Security Rules

The implementation must ensure:

- Raw passwords are accepted only at the application boundary.
- Raw passwords are never stored.
- Raw passwords are never logged.
- Password hashes are never exposed through API responses.
- Password hashes are never written to audit logs.
- Audit logs should record the event type, user ID, actor if available, and timestamp, but not secrets.

## Completion Criteria

This phase is complete when:

- Password setting and update logic exists in the application service layer.
- Passwords are encoded before persistence.
- Password metadata is updated correctly.
- Password-related audit events are created safely.
- Tests verify password update behavior.
- Tests verify that sensitive values are not exposed through response DTOs.

## Progress Notes

- PasswordEncoder configuration and application-layer password management use cases added.
- Raw passwords are validated at the application boundary and encoded before persistence.
- Password API, login flow, MFA-in-login, and OAuth2 Authorization Code Flow remain future work.

## Out of Scope

This phase does not include:

- Login authentication.
- Password reset email.
- Password history policy.
- Breached password detection.
- Rate limiting.
- Account lockout by failed login attempts.

---

# Phase 3: Minimal Password Management API

## Goal

Expose minimal backend APIs for password setup and password update.

This phase provides API-level access for password management while keeping the scope limited to backend/admin-style operations.

## Scope

Possible API endpoints:

- `POST /api/users/{id}/password`
- or `PUT /api/users/{id}/password`

The final endpoint style should follow the existing controller conventions.

The request DTO may include:

- `newPassword`
- `passwordResetRequired`

The response should not include password-sensitive data.

## Completion Criteria

This phase is complete when:

- A password setup or update endpoint exists.
- Request validation handles missing or invalid passwords.
- Response DTOs do not expose password hashes.
- Controller tests cover success and validation failure cases.
- OpenAPI documentation shows the endpoint clearly.
- Existing APIs remain backward compatible where possible.

## Progress Notes

- Minimal admin-style password update API added at `PUT /api/users/{id}/password`.
- The endpoint reuses application-layer password validation, encoding, metadata updates, and safe audit logging.
- Password validation errors are mapped to HTTP 400 responses.
- Password hashes, raw passwords, credential metadata, and reset flags are not returned in user API responses.
- Login, self-service password reset, and OAuth2 Authorization Code Flow remain future work.

## Out of Scope

This phase does not include:

- Public self-service password reset.
- Email verification.
- Login endpoint.
- Frontend forms.
- OAuth2 authorization endpoint customization.

---

# Phase 4: Spring Security Local User Authentication Foundation

## Goal

Connect the user and password credential model to Spring Security authentication.

This phase should make it possible for the backend to authenticate a local user using username and password.

## Scope

This phase may include:

- Custom `UserDetailsService` or equivalent authentication user loading.
- Password verification through `PasswordEncoder`.
- Account status checks.
- Minimal authentication success and failure behavior.
- Authentication-related audit events.
- Tests for successful and failed authentication.

## Important Design Decision

This phase should decide whether the project will initially use:

- Spring Security form login for browser-based login readiness, or
- a backend-only authentication endpoint for testing authentication behavior.

For an OAuth2 Authorization Server project, minimal form login is usually a better foundation because Authorization Code Flow requires an interactive user authentication step.

## Completion Criteria

This phase is complete when:

- A local user can be authenticated with username and password.
- Disabled or locked users cannot authenticate.
- Password verification uses the stored password hash.
- Authentication failures do not leak sensitive information.
- Authentication events are audited safely.
- Tests cover successful login, wrong password, disabled user, and missing user scenarios.

## Progress Notes

- Spring Security local user authentication foundation added through a platform `UserDetailsService`, local `AuthenticationProvider`, and `AuthenticationManager`.
- Authentication uses the stored password hash with the configured `PasswordEncoder`.
- Only `ACTIVE` users with a valid password credential can authenticate.
- `DISABLED`, `LOCKED`, `PENDING`, missing users, users without password credentials, and wrong passwords are rejected with a generic authentication failure.
- Existing-user authentication success and failure events are audited without raw passwords or password hashes.
- MFA login challenge, OAuth2 Authorization Code Flow, consent, and frontend login UI remain future work.

## Out of Scope

This phase does not include:

- MFA challenge.
- Remember-me.
- Password reset.
- Advanced session management.
- OAuth2 consent page.
- Full frontend login UI.
- Social login.

---

# Phase 5: Minimal Login Flow Readiness

## Goal

Prepare the project for browser-based login required by OAuth2 Authorization Code Flow.

This phase should not build a full frontend. It should provide the smallest acceptable login mechanism for backend and protocol testing.

## Scope

This phase may include:

- Minimal server-rendered login page, if needed.
- Spring Security login configuration.
- Clear separation between Authorization Server endpoints and management API endpoints.
- Test coverage for protected pages or endpoints.
- Documentation explaining the login flow.

## Completion Criteria

This phase is complete when:

- The backend has a minimal login flow.
- A user can authenticate through the configured login mechanism.
- Login state can support future `/oauth2/authorize` interaction.
- Public endpoints and protected endpoints are clearly separated.
- Tests verify that protected resources require authentication.
- Documentation explains that this is a minimal login foundation, not a full user-facing frontend.

## Out of Scope

This phase does not include:

- React/Vue admin console.
- Full UI/UX design.
- User registration page.
- Self-service account recovery.
- MFA login challenge.
- Consent screen.

---

# Phase 6: OAuth2 Registered Client Persistence Foundation

## Goal

Upgrade the current `Client` model from a simple business record into a foundation for real OAuth2 registered clients.

The project currently has a Spring Authorization Server baseline, but registered clients are not fully persisted through the project domain model.

## Scope

This phase should include a careful design for client persistence, including:

- Client ID.
- Client secret hash or encoded secret.
- Client type.
- Client authentication methods.
- Authorization grant types.
- Redirect URIs.
- Post-logout redirect URIs if needed later.
- Scopes.
- Consent requirement.
- Token settings.
- Client settings.
- Tenant association.

The implementation should remain MVP-sized and should not attempt to support every OAuth2 feature immediately.

## Completion Criteria

This phase is complete when:

- The client domain model can represent a minimal OAuth2 registered client.
- Client secrets are never stored in plaintext.
- Redirect URIs and grant types are persisted.
- Scopes are persisted.
- Client DTOs do not expose stored secrets.
- Tests verify persistence and safe response behavior.
- Documentation explains the difference between platform `Client` and Spring Authorization Server `RegisteredClient`.

## Out of Scope

This phase does not include:

- Dynamic client registration.
- Client secret rotation UI.
- Full admin console for clients.
- Full Authorization Code Flow.
- Full consent implementation.

---

# Phase 7: RegisteredClientRepository Integration MVP

## Goal

Connect the persisted client model to Spring Authorization Server's `RegisteredClientRepository`.

This phase bridges the project's own `Client` domain model with Spring Authorization Server's runtime model.

## Scope

This phase may include:

- Mapping project `Client` records to Spring Authorization Server `RegisteredClient`.
- Replacing or reducing the current in-memory registered client setup.
- Supporting lookup by `clientId`.
- Supporting required grant types and scopes for MVP.
- Tests for registered client loading.

## Completion Criteria

This phase is complete when:

- Spring Authorization Server can load registered clients from persistent storage.
- The development client no longer depends only on hardcoded in-memory configuration.
- Client secret encoding is compatible with Spring Security expectations.
- Tests verify that a persisted client can be loaded as a `RegisteredClient`.
- Documentation explains the current persistence approach and its limitations.

## Out of Scope

This phase does not include:

- Full OAuth2 Authorization Code Flow.
- Consent persistence.
- Dynamic client registration.
- Advanced token customization.
- Multi-issuer tenant-aware authorization server design.

---

# Phase 8: Authorization Code Flow Preparation

## Goal

Prepare the platform for a minimal Authorization Code Flow without trying to complete the full production-grade flow in one step.

This phase should verify that the previous foundations are sufficient for a future end-to-end authorization flow.

## Scope

This phase should focus on:

- Required client fields for authorization code.
- Redirect URI validation readiness.
- PKCE readiness for public clients.
- Login dependency readiness.
- Consent model design notes.
- Documentation of the intended flow.

## Completion Criteria

This phase is complete when:

- The project clearly supports the data needed for Authorization Code Flow.
- The login foundation can support future `/oauth2/authorize` requests.
- Registered client persistence can support authorization-code clients.
- The next implementation steps for Authorization Code Flow are documented.
- Known gaps are listed explicitly.

## Out of Scope

This phase does not include:

- Full consent screen.
- Full OIDC ID Token customization.
- UserInfo endpoint.
- Refresh token lifecycle.
- Token revocation.
- Token introspection.
- Production-grade OAuth2 security hardening.

---

# Phase 9: Documentation and Portfolio Alignment

## Goal

Keep the public project understandable, honest, and useful as a portfolio project.

This phase ensures that the roadmap, security design, architecture, and README reflect the actual implementation status.

## Scope

Documentation should update:

- `README.md`
- `docs/PROJECT_VISION.md`
- `docs/ROADMAP.md`
- `docs/ARCHITECTURE.md`
- `docs/SECURITY_DESIGN.md`
- The new authentication roadmap document

## Completion Criteria

This phase is complete when:

- Documentation clearly says what is implemented.
- Documentation clearly says what is not implemented.
- The project does not overclaim production readiness.
- The roadmap shows the next recommended stage.
- Security-sensitive design decisions are recorded.
- The English writing is clear and suitable for a public portfolio project.

---

# Explicitly Not Planned in This Roadmap Cycle

The following features are important, but should not be implemented in this medium-term roadmap cycle:

- Full frontend admin console.
- Full user self-service portal.
- Full OAuth2 Authorization Code Flow production completion.
- Full OIDC certification-level implementation.
- MFA challenge during login.
- Password reset by email.
- Account recovery.
- Risk-based authentication.
- Social login.
- SAML federation.
- Enterprise external IdP federation.
- Dynamic client registration.
- Complete SCIM 2.0 lifecycle.
- Token revocation and introspection.
- Advanced session management.
- Kubernetes deployment.
- Production secret management system.

These features should be considered after the authentication and registered client foundations become stable.

---

# Recommended Execution Order for ChatGPT and Codex

This medium roadmap should be executed as many small Codex tasks.

A recommended breakdown is:

1. Add password credential fields and account status.
2. Add password encoding configuration.
3. Add password management application service logic.
4. Add password management API.
5. Add password-related tests and safe DTO checks.
6. Add Spring Security local user loading.
7. Add minimal authentication behavior.
8. Add minimal login readiness.
9. Expand OAuth2 client domain model.
10. Add client secret handling.
11. Add grant type, redirect URI, and scope persistence.
12. Integrate persisted clients with `RegisteredClientRepository`.
13. Prepare Authorization Code Flow documentation and tests.
14. Update roadmap, security design, and architecture docs.

Each item should be converted into a separate Codex prompt only when the previous item is reviewed and understood.

---

# Definition of Done for This Roadmap Cycle

This roadmap cycle is complete when:

- Users can have password credentials.
- Passwords are securely hashed.
- Password management is available through application logic and minimal APIs.
- Basic account status exists.
- Local username/password authentication is connected to Spring Security.
- The backend has minimal login readiness.
- OAuth2 clients can be persisted with the fields needed for authorization code clients.
- Spring Authorization Server can load registered clients from persistent storage.
- Authorization Code Flow prerequisites are clearly prepared.
- Documentation accurately reflects the new project state.
- The project is ready for the next roadmap cycle: Minimal Authorization Code Flow and Consent.

---

# Next Roadmap Cycle

After this roadmap is complete, the next medium-term roadmap should focus on:

**Minimal Authorization Code Flow and Consent**

That future cycle should include:

- End-to-end Authorization Code Flow.
- PKCE support.
- Consent persistence.
- Minimal consent page.
- OIDC ID Token basics.
- UserInfo endpoint foundation.
- Better token lifecycle documentation.
- More complete OAuth2 integration tests.
