# clazz-lms

A Spring Boot learning management system — started as a tutorial
project, currently being refactored toward production quality in a
series of atomic PRs. My first end-to-end backend project, and an
ongoing sandbox for engineering practices I'm learning along the way.

**Status:** Active. 11 merged PRs as of May 2026.

**Long-term goal:** Evolve this from a learning sandbox into a
production-deployable school administration system.

**Stack:** Java 17, Spring Boot 3.5.13, MyBatis, MySQL 8, Maven
multi-module, JWT (jjwt 0.12), BCrypt, JUnit + H2 (test-scoped).

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
- JWT-based authentication with `Authorization: Bearer <token>` headers
- BCrypt password hashing with verified data migration from plain-text
- Centralized exception handling with structured `ErrorResponseDTO`
  responses
- Audit logging of write operations via AOP
- OpenAPI/Swagger documentation via Knife4j

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
├── exception/                   Custom exceptions + GlobalExceptionHandler
├── interceptor/                 TokenInterceptor (JWT validation)
├── security/                    JwtService, PasswordEncoder bean
├── config/                      WebConfig, Knife4j config
├── aop/                         LogAspect (operation auditing)
└── anno/                        Custom annotations (@Log)
```

### Request flow

```
HTTP request
   |
   v
TokenInterceptor (whitelist: /login, /doc.html, etc.)
   |  reads Authorization: Bearer <jwt>
   |  validates via JwtService, sets BaseContext
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

## Getting Started

### Prerequisites

- Java 17 (the project uses Java 17 features)
- Maven 3.6+ (for build; or use IntelliJ's bundled Maven)
- MySQL 8.x (running on `localhost:3306` for default config)
- IntelliJ IDEA recommended (the project includes `.idea/` config)

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

The server listens on `http://localhost:8080`. Swagger UI is available
at `http://localhost:8080/doc.html`.

### 5. Verify

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username": "<your-test-user>", "password": "<their-password>"}'
```

A successful login returns a JWT token; subsequent requests include
it via the `Authorization: Bearer <token>` header.

## Engineering Highlights

The project has been refactored across 11 atomic PRs since May 2026,
each documented with a clear motivation, design decisions, and
verification steps. Selected highlights:

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

- **Schema migration tooling**: Commit a versioned `schema.sql`
  (or migrate to Flyway / Liquibase for proper schema versioning).
- **Spring Security migration**: Replace the custom `TokenInterceptor`
  with `OncePerRequestFilter` + `AuthenticationEntryPoint`. Adopt
  `DelegatingPasswordEncoder` to support future algorithm migrations
  (e.g., BCrypt → Argon2) without another dual-write series.
- **Refresh token + access token**: Currently access tokens are
  long-lived (12h) with no revocation. Plan: short-lived (15min)
  access tokens, long-lived (7d) refresh tokens stored in Redis with
  per-user revocation support.
- **Role-based access control (RBAC)**: Currently any authenticated
  user can hit any endpoint. Plan: integrate `Emp.job` (1=head
  teacher, 2=lecturer, …) with Spring Security authorities.
- **File upload via Aliyun OSS**: Configuration is in place; upload
  endpoints to be implemented.
- **Integration tests for auth flow**: Existing tests cover Dept
  endpoints; coverage for Login + Emp CRUD is pending.
- **DTO cleanups**: `EmpLoginDTO.@Size(max=32)` is too restrictive
  for strong passwords; message-vs-constraint mismatches in
  `username` validation message ("must not exceed 50" vs `max=20`).

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