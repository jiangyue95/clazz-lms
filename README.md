# clazz-lms

A Spring Boot learning management system — started as a tutorial
project, currently being refactored toward production quality in a
series of atomic PRs. My first end-to-end backend project, and an
ongoing sandbox for engineering practices I'm learning along the way.

**Status:** Active. 21 merged PRs as of June 2026.

**Long-term goal:** Evolve this from a learning sandbox into a
production-deployable school administration system.

**Stack:** Java 17, Spring Boot 3.5.13, MyBatis, MySQL 8, Maven
multi-module, JWT (jjwt 0.12), BCrypt (spring-security-crypto), springdoc-openapi,
JUnit + H2 (test-scoped).

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Engineering Highlights](#engineering-highlights)
- [Roadmap](#roadmap)
- [Project Origin](#project-origin)
- [License](#license)

## Overview

clazz-lms manages the day-to-day data of a training school:
departments, classes, employees (teachers, head teachers, advisors),
and students. The current scope includes:

- CRUD APIs for departments, classes, employees, and students
- Self-service authentication: registration, login, token refresh, and 
  password change
- Dual-token JWT auth: a short-lived access token plus a refresh token
  stored in Redis (SHA-256 hashed), with per-user revocation
- BCrypt password hashing with verified data migration from plain-text
- Centralized exception handling with structured `ErrorResponseDTO`
  responses
- Audit logging of write operations via AOP
- OpenAPI/Swagger documentation via springdoc-openapi

The codebase is deliberately incrementally improved — each PR is a
focused refactor with a coherent story, atomic commits, and a
description that captures the decisions made.

## Architecture

### Multi-module layout

```
clazz-lms/
├── clazz-lms-pojo/      Entities, DTOs (request), VOs (response)
├── clazz-lms-utils/     Shared utilities (BaseContext for thread-local user)
└── clazz-lms-server/    Web layer (controllers, services, mappers, config)
```

The `pojo` module is intentionally separate from `server` so that
DTOs and VOs can be reused across hypothetical clients without
dragging in web-layer dependencies.

### Server-module structure

```
com.yue/
├── ClazzLmsApplication.java     Spring Boot entry point
├── controller/                  REST controllers
├── service/                     Business logic
├── mapper/                      MyBatis mappers
├── repository/                  RefreshTokenRepository (Redis-backed)
├── security/                    JwtService, SecurityConfig, JwtConfigProperties
├── interceptor/                 TokenInterceptor (JWT validation)
├── exception/                   Custom exceptions + GlobalExceptionHandler
├── config/                      WebConfig, OpenApiConfig, CorsConfig
├── aop/                         LogAspect (operation auditing)
└── anno/                        Custom annotations (@Log)
```

### Request flow

```
HTTP request
   |
   v
TokenInterceptor (whitelist: /login, /register, /refresh, swagger routes)
   |  reads Authorization: Bearer <jwt>
   |  validates via JwtService, checks token_type == "access"
   |  sets BaseContext (thread-local empId)
   v
@RestController method
   |  @Valid triggers Bean Validation
   v
Service layer (business logic, @Transactional where needed)
   |
   v
Mapper (MyBatis) → MySQL
   |
   v
Response wrapping → JSON via Jackson
   |
   v
On error: GlobalExceptionHandler → ErrorResponseDTO
```

Refresh tokens live outside the flow: they are issued at login, stored 
in Redis, and exchanged for a new access token at `/refresh`.

## Getting Started

### Prerequisites

- Java 17 (the project uses Java 17 features)
- Maven 3.6+ (for build; or use IntelliJ's bundled Maven)
- MySQL 8.x (running on `localhost:3306` for default config)
- IntelliJ IDEA recommended (the project includes `.idea/` config)
- Redis (for refresh-token storage; default `localhost:6379`)

### 1. Clone the repository

```bash
git clone https://github.com/jiangyue95/clazz-lms.git
cd clazz-lms
```

### 2. Set up MySQL

Create the database:

```sql
CREATE DATABASE tlias CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> The schema DDL is not yet committed to the repository. Tables can be
> derived from entity classes in `clazz-lms-pojo/`, but a versioned
> `schema.sql` is a planned improvement (see Roadmap).

### 3. Configure application.yml

Copy the example config and fill in your local values:

```bash
cp clazz-lms-server/src/main/resources/application.example.yml \
   clazz-lms-server/src/main/resources/application.yml
```

Then edit `application.yml`:

- `spring.datasource.username` / `password` — your MySQL credentials
- `spring.data.redis.host` / `port` - your Redis connection
- `jwt.secret` — any string at least 32 characters long (for HMAC-SHA256
  signing)

The real `application.yml` is gitignored and never committed — only
the `.example.yml` template is in version control.

### 4. Run the application

From IntelliJ IDEA: open `ClazzLmsApplication.java` and click the
green Run button.

From the command line:

```bash
mvn -pl clazz-lms-server spring-boot:run
```

The server listens on `http://localhost:8080`. The interactive Swagger 
UI is available at `http://localhost:8080/swagger-ui.html`, and the 
raw OpenAPI 3 spec at `http://localhost:8080/v3/api-docs`.

### 5. Verify

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username": "<your-test-user>", "password": "<their-password>"}'
```

A successful login returns an access token and a refresh token; subsequent
requests include the access token via the `Authorization: Bearer <token>` 
header. When the access token expires, the client exchanges the refresh token
at `POST /refresh` for a new one instead of forcing a re-login.

## Engineering Highlights

The project has been refactored across 21 atomic PRs since May 2026,
each documented with a clear motivation, design decisions, and
verification steps. Selected highlights:

### Dual-token authentication with Redis-backed refresh tokens ([PR #14](https://github.com/jiangyue95/clazz-lms/pull/14))
The old design used one access token with a long lifetime. This release
splits it into two tokens: a short-lived access token for normal requests,
and a refresh token whose only job is to obtain a new access token.
Refresh tokens are never stored in raw form. Each token is hashed with
SHA-256, and only the hash is kept in Redis. SHA-256 is the right choice
here, not BCrypt. A refresh token is a long, random value generated by
the server, so it cannot be guessed by brute force. BCrypt is deliberately
slow to protect weak, human-chosen passwords -- and that slowness
buys nothing for a value that is already unguessable.
Redis also keeps a per-user index: the key `refresh_user:{empId}`
holds the set of that user's token hashes. This index is what makes it
possible to revoke all of one user's tokens at once, for example when
they change their password.
Two mechanisms control how long a token lives. Each Redis key is given
a TTL equal to the time left until the token's `expiresAt`, so expired
tokens are deleted automatically with no cleanup job. A separate 
`revokedAt` field lets a token be invalidated early, before it would expire
on its own.
Storage uses `StringRedisTemplate` with explicit Jackson serialization,
rather than `@RedisHash` repositories. The reason is debuggability: the
value saved in Redis stays as readable JSON, so it can be inspected
directly with `redis-cli`.

### Self-service auth: registration and password change with session revocation ([PR #13](https://github.com/jiangyue95/clazz-lms/pull/13))
Added `POST /register` and `PATCH /me/password`. Changing a password
revokes all of that user's refresh tokens, so any stolen token is invalidated
immediately rather than lingering until natural expiry. The revocation is 
deliberately ordered last in the method and wrapped in a try-catch:
`@Transactional` rolls back the MySQL write on an uncaught exception, but 
Redis is not transactional -- performing the Redis revoke after the DB
commit, and swallowing any Redis failure, prevents a state where the
password is rolled back while the tokens are already gone.

### BCrypt password migration via dual-write transition ([PR #11](https://github.com/jiangyue95/clazz-lms/pull/11))

Migrated all employee password storage from plain-text to BCrypt
hashes in 6 atomic commits, with zero downtime: existing logins
worked at every point during the transition. The migration used the
dual-write pattern (server accepts both formats during the migration
window), a one-shot Spring `ApplicationRunner` that was deleted
before the commit (keeping migration machinery out of permanent
code), and idempotent design (rows already in BCrypt format were
skipped, making the migration safe to re-run after interruption).

### Standardized auth: Bearer header and structured 401 responses ([PR #10](https://github.com/jiangyue95/clazz-lms/pull/10))

Migrated from a custom `token: <jwt>` header to the standard
`Authorization: Bearer <jwt>` (RFC 6750), and unified the 401
response body so that `TokenInterceptor` and `GlobalExceptionHandler`
produce the same `ErrorResponseDTO` shape. Internal logs distinguish
the failure modes (missing / malformed / invalid) for debugging; the
external response is uniform to avoid leaking which failure
occurred.

### Exception-based error handling and user-enumeration defense ([PR #5](https://github.com/jiangyue95/clazz-lms/pull/5))

Replaced `return null` from the login service with a typed
`InvalidCredentialsException`, mapped to HTTP 401 via
`GlobalExceptionHandler`. The implementation specifically defends
against user enumeration: both "username not found" and "wrong
password" branches log the precise cause internally but return
identical 401 responses externally.

### JWT configuration externalization ([PR #4](https://github.com/jiangyue95/clazz-lms/pull/4))

Migrated JWT signing secret and expiration from compile-time
constants to `application.yml`, validated via
`@ConfigurationProperties`. Removed dead static-method `JWTUtils` in
favor of a Spring-managed `JwtService` bean, simplifying both
testability and configuration management.

### REST conventions across all CRUD controllers (PRs [#5](https://github.com/jiangyue95/clazz-lms/pull/5), [#6](https://github.com/jiangyue95/clazz-lms/pull/6), [#7](https://github.com/jiangyue95/clazz-lms/pull/7), [#9](https://github.com/jiangyue95/clazz-lms/pull/9))

Aligned `LoginController`, `ClazzController`, `EmpController`, and
read-only controllers (`LogController`, `ReportController`) with
REST conventions: proper HTTP status codes (201 Created, 204 No
Content, 404 Not Found, 409 Conflict), `Location` headers on POST
responses, and `ResponseEntity<T>` return types throughout.

### Audit-discovered fix: NoResourceFoundException handler ([PR #8](https://github.com/jiangyue95/clazz-lms/pull/8))

While auditing exception handling, discovered that unmatched routes
were producing default Spring error pages instead of
`ErrorResponseDTO` JSON. Added an explicit `@ExceptionHandler` for
`NoResourceFoundException` to ensure all 404s share the project's
unified response shape.

For the full list, see the [merged PRs on GitHub](https://github.com/jiangyue95/clazz-lms/pulls?q=is%3Apr+is%3Amerged).

## Roadmap

Planned improvements, roughly in order of priority:

- **Spring Security migration**: Replace the custom `TokenInterceptor`
  with `OncePerRequestFilter` + `AuthenticationEntryPoint`. The
  project currently depends only on `spring-security-crypto` (for
  BCrypt), not `spring-boot-starter-security`, to avoid activating
  autoconfiguration (formLogin, httpBasic, CSRF) that would conflict
  with the interceptor flow. Adopting the full filter chain is a separate,
  larger refactor.
- **`DelegatingPasswordEncoder`**: `SecurityConfig` already exposes the
  encoder via the `PasswordEncoder` interface, so migrating BCrypt ->
  Argon2 (with `{bcrypt}` / `{argon2}` format prefixes) would be a near
  single-line change with no dual-write series needed. 
- **Role-Based access control (RBAC)**: Currently any authenticated user
  can hit any endpoint. Plan: integrate `Emp.job` (1=head teacher,
  2=lecturer, ...) with role-gated authorities.
- **Schema migration tooling**: Commit a versioned `schema.sql`
  (or migrate to Flyway / Liquibase for proper schema versioning).
- **File upload via AWS S3**: UploadController is in place; the 
  previous Aliyun OSS configuration has expired and will be replaced 
  with S3.
- **Integration tests beyond Dept**: `DeptControllerIntegrationTest`
  covers the Dept endpoints; coverage for Emp, Clazz, Student, and the
  auth flow is pending.

## Project Origin

This project started from an online Spring Boot tutorial — the kind
that walks you through CRUD APIs and shows multiple ways to wire up
the same feature. The tutorial provided a working baseline, but the
code carried the typical patterns of beginner backend work:
plain-text passwords, hardcoded defaults, null-return from service
layers, inconsistent error responses, custom HTTP headers where
standards exist.

Since then, I've been refactoring it in series — one focused PR at a
time, each with an explicit motivation and design notes — toward
what I'd want to see in a production codebase. The work is paired
with deliberate practice of professional engineering habits: atomic
commits, descriptive messages, scope discipline, pre-flight
verification, backups before irreversible operations.

I work on it primarily as a portfolio project while preparing for
Software Development Engineer roles in Ireland. Improvements happen
in evening sessions over several weeks, often with a cooling-off
period between writing code and merging the PR the next morning.

## License

This project is licensed under the MIT License — see the
[LICENSE](LICENSE) file for details.