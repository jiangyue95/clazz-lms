# AGENTS.md — clazz-lms

## Project Overview

Java 17 / Spring Boot 3.5.13 Maven multi-module LMS (Learning Management System).
Group: `com.yue`, Root artifact: `clazz-lms`.

## Module Structure

| Module             | Role                                                                     |
|--------------------|--------------------------------------------------------------------------|
| `clazz-lms-pojo`   | DTOs, VOs, entities (Lombok `@Data` beans), validation annotations       |
| `clazz-lms-utils`  | `JWTUtils`, `AliyunOssUtil`, `BaseContext` (ThreadLocal current user ID) |
| `clazz-lms-server` | Application entry point, controllers, services, mappers, AOP             |

**Dependency order**: `pojo` + `utils` → `server`. Always build from the **root** so Maven resolves sibling modules.

## Commands

```bash
mvn clean install          # Build all modules (required before running server)
mvn spring-boot:run -pl clazz-lms-server   # Run dev server
mvn test                   # Run all tests (H2 in-memory, test profile)
mvn test -pl clazz-lms-server -Dtest=DeptControllerIntegrationTest  # Single test
```

No separate lint/typecheck steps. Maven compile is the verification gate.

## Architecture

- **Layering**: Controller → Service → Mapper (MyBatis XML mappers in `resources/com/yue/mapper/`)
- **Entry point**: `ClazzLmsApplication` in `clazz-lms-server`
- **Base package**: `com.yue` (all sub-packages live under this)
- **Controllers**: `DeptController`, `EmpController`, `ClazzController`, `StudentController`, `LoginController`, `UploadController`, `LogController`, `ReportController`
- **API docs**: Knife4j at `/doc.html` (Swagger UI at `/swagger-ui.html`)

## Auth Flow (IMPORTANT)

- **`TokenInterceptor`** is the active auth mechanism, registered in `WebConfig`.
- Reads the `token` header (NOT `Authorization: Bearer`).
- Validates JWT, sets user ID into `BaseContext` (ThreadLocal).
- Excludes: `/login`, `/favicon.ico`, `/doc.html`, `/v3/api-docs/**`, `/swagger-ui/**`, `/webjars/**`.
- **`SecurityConfig`** with `JwtAuthenticationFilter` exists but is **incomplete** (code does not compile as-is). Do not rely on Spring Security for auth.
- When writing integration tests, mock `TokenInterceptor` with `@MockitoBean` to return `true`.

## Database

- **Dev**: MySQL, database `tlias` on `localhost:3306`. Config in `application.yml` (gitignored).
- **Test**: H2 in-memory with `MODE=MySQL`. Profile `test` activates via `application-test.yml`.
- Test schema/data auto-init from `schema-test.sql` and `data-test.sql` in `src/test/resources`.
- MyBatis XML mapper files live at `src/main/resources/com/yue/mapper/*.xml`.
- `map-underscore-to-camel-case: true` is enabled.

## Testing

- Framework: JUnit 5 + Spring Boot Test + MockMvc.
- Integration tests use `@ActiveProfiles("test")`, `@Transactional` (auto-rollback), H2 DB.
- **Always mock `TokenInterceptor`** in controller tests — it intercepts `/**` and will reject requests without a valid token.
- The `operate_log` table is required in test schema because `@Log` AOP annotation on controllers writes to it after every annotated method.

## Key Conventions

- **Exception model**: Custom exceptions (`ResourceNotFoundException`, `ValidationException`, `BusinessRuleViolationException`, etc.) extend `BaseException` with `errorCode` strings. `GlobalExceptionHandler` maps them to `ErrorResponseDTO` with appropriate HTTP status codes.
- **Response wrapper**: `Result` class with `code` (1=success, 0=fail), `msg`, `data`.
- **Pagination**: PageHelper via `PageHelper.startPage(page, pageSize)` before mapper calls.
- **File upload**: Aliyun OSS, max 10MB per file and per request.
- **CORS**: Allows `http://localhost:5173` only (Vite dev server).
- **AOP**: `LogAspect` logs operations annotated with `@Log` into `operate_log` table.
- **Lombok**: Used everywhere. Annotation processor configured in `maven-compiler-plugin`.

## Prerequisites for Running

- MySQL running locally with a `tlias` database.
- Redis on `localhost:6379` (configured but may not be actively used).
- Copy `application.example.yml` → create your own `application.yml` with real credentials.

## Gotchas

- `application.yml` is gitignored and contains real credentials in the working copy. Never commit it.
- `SecurityConfig` is incomplete code — do not try to use it as a reference for Spring Security patterns.
- When adding new controller endpoints, remember: if the controller class or method has `@Log`, the `operate_log` table must exist for tests to pass.
- `WebConfig` says "Not in use, this is an example" in its comment but **it IS active** — the `TokenInterceptor` is registered there.
