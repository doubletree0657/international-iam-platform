# Security Design

## Overview

The security design is production-inspired and intentionally scoped for an IAM backend foundation prototype.

The project demonstrates OAuth2 concepts, JWT-based API protection, tenant boundary checks, audit logging, TOTP MFA, and MFA secret encryption. It does not claim production-grade IAM security.

## OAuth2 Authorization Server

Spring Authorization Server provides the OAuth2 foundation. The current implementation supports registered clients, token issuing foundations, and integration points for future login and authentication improvements.

This is a foundation, not a complete product-grade authorization server deployment.

## JWT And JWK Support

Access tokens are represented as JWTs. JWK support exposes signing-key metadata needed by clients or resource servers that validate tokens.

Future production-oriented work would need stronger key lifecycle management, including key rotation, external key storage, operational controls, and incident response procedures.

## Scope-Based API Authorization

Management APIs under `/api/**` and SCIM APIs under `/scim/v2/**` require OAuth2 JWT scopes.

- Read operations require `iam.read`.
- Write operations require `iam.write`.
- The health endpoint remains public for local checks.

This demonstrates coarse-grained API authorization. More detailed authorization decisions may be added in future phases.

## Tenant Boundary Validation

Tenant boundary validation is handled in application services. Current checks prevent cross-tenant role assignment and validate tenant consistency in group membership operations.

These checks are treated as business rules, not just web-layer concerns, because tenant isolation must be enforced close to the use cases that change state.

## Audit Logging

Audit logging records important IAM and administration events, including user, role, permission, client, MFA, and SCIM-related activity.

The current audit design supports traceability and interview discussion. It is not yet a complete compliance logging, retention, alerting, or SIEM integration solution.

## MFA And Secret Protection

The project includes TOTP-based MFA enrollment and verification. MFA verification has targeted test coverage, and stored MFA secrets are encrypted before persistence.

The current encryption setup is suitable for local development and prototype discussion. Production use would require stronger key management, rotation, access control, monitoring, and recovery procedures.

## Current Security Boundaries

- `/api/health` is public.
- `/api/**` requires JWT scope authorization.
- `/scim/v2/**` requires JWT scope authorization.
- Application services enforce tenant consistency for selected workflows.
- Sensitive values such as MFA secrets are not returned in normal user responses.

## Not Production-Grade Yet

The project intentionally does not yet include:

- Complete production login and account lifecycle flows.
- Enterprise-grade key rotation.
- External secret management.
- Full token lifecycle controls.
- Complete fine-grained authorization policy.
- Production observability and alerting.
- Deployment hardening.
- Complete SCIM enterprise behavior.

## Security Principles

- Do not store passwords, client secrets, tokens, or MFA secrets in plain text.
- Do not log tokens, passwords, client secrets, or MFA secrets.
- Keep tenant boundary checks explicit and testable.
- Keep local credentials separate from production secrets.
- Do not commit real CI/CD secrets.
- Prefer clear, reviewable security behavior over hidden complexity.
