# Security Design

## Overview

The security design is production-inspired but intentionally scoped for a portfolio project. It demonstrates OAuth2, JWT-based API protection, tenant boundary checks, audit logging, and MFA concepts without claiming to be a production IAM system.

## OAuth2 Authorization Server

The project includes Spring Authorization Server support. This provides the foundation for OAuth2 flows, registered clients, token issuing, and future authentication improvements.

## JWT and JWK Support

Access tokens are represented as JWTs. JWK support provides signing-key metadata needed by resource servers and clients that validate tokens.

This is currently suitable for learning and demonstration. Production key rotation, external key management, and operational controls are future work.

## Scope-Based API Authorization

Management APIs under `/api/**` and SCIM APIs under `/scim/v2/**` require OAuth2 JWT scopes.

- Read operations require `iam.read`.
- Write operations require `iam.write`.
- The health endpoint remains public for simple local checks.

## Tenant Boundary Validation

Tenant boundary validation is handled in the application service layer. The project prevents cross-tenant role assignment and validates tenant consistency in group membership operations.

This keeps authorization-sensitive business rules close to the use cases that need them.

## Audit Logging

Audit logging records important IAM and administration events, such as user creation, role assignment, permission changes, client creation, and authentication-related activity.

The audit model supports interview discussion about traceability, security review, and operational visibility.

## MFA TOTP

The project includes TOTP-based MFA enrollment and verification. The implementation follows standard TOTP concepts and includes test coverage for known verification behavior.

## MFA Secret Encryption

Stored MFA secrets are encrypted before persistence. Local development keeps key configuration simple, while the design leaves room for stronger production secret management later.

## Not Production-Grade Yet

The project intentionally does not yet include:

- Production-ready user login and account lifecycle flows.
- Enterprise-grade key rotation.
- External secret management.
- Full production observability and alerting.
- Deployment hardening.
- Complete SCIM enterprise behavior.

## Security Principles

- Do not store passwords, client secrets, tokens, or MFA secrets in plain text.
- Do not log tokens, passwords, client secrets, or MFA secrets.
- Keep tenant boundary checks explicit and testable.
- Keep local credentials separate from production secrets.
- Do not commit real CI/CD secrets.
- Prefer clear, explainable security design over hidden complexity.
