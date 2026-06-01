package com.yue.exception;

import com.yue.pojo.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler for all REST controllers.
 *
 * <p>Acts as a centralised translation layer; domain and framework
 * exceptions thrown anywhere below the controller layer are caught here
 * and converted into a uniform {@link ErrorResponseDTO} response with
 * the appropriate HTTP status code.
 *
 * <p>Handler organisation:
 * <ul>
 *     <li>Domain exceptions (e.g. {@code ResourceNotFoundException},
 *         {@code BusinessRuleViolationException}) - each maps to a single
 *         HTTP status code defined by the business meaning of the exception.</li>
 *     <li>Framework exceptions (e.g. {@code MethodArgumentNotValidException},
 *         {@code NoResourceFoundException}, {@code DuplicateKeyException},
 *         {@code HttpMessageNotReadableException}) - each handled to ensure
 *         client-side errors return proper 4xx codes instead of falling through
 *         to the catch-all 500.</li>
 *     <li>{@code Exception.class} catch-all - last-resort handler, returns
 *         500 with a sanitised generic message.</li>
 * </ul>
 *
 * <p>Logging discipline: client errors (4xx) are logged at {@code WARN};
 * server errors (5xx) at {@code ERROR}. This keeps monitoring noise low
 * - invalid client input shouldn't page on-call.
 *
 * <p>Information disclosure discipline: detailed exception messages are
 * logged server-side for diagnostics but never echoed back in the
 * response. Schema details, parser internals, and stack traces stay
 * internal.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles requests for a resource that does not exist int the domain
     * (e.g. {@code GET /clazzs/999} where id 999 is not in the database).
     *
     * @param ex the not-found exception, carrying its own errorCode and message
     * @param request current HTTP request (used to populate the path field)
     * @return 404 Not Found with the exception's errorCode and message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    /**
     * Handles authentication failures - requests that lack valid
     * credentials. Typically, thrown when a token is missing, malformed,
     * or signature-invalid.
     *
     * @param ex an unauthorised exception, carrying its own errorCode and message
     * @param request current HTTP request (used to populate the path field)
     * @return 401 Unauthorized with the exception's errorCode and message
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
    }

    /**
     * Handles authorisation failures - requests that are authenticated
     * but lack permission for the requested operation (e.g. a teacher
     * accessing an admin-only endpoint).
     *
     * @param ex the forbidden exception, carrying its own errorCode and message
     * @param request current HTTP request (used to populate the path field)
     * @return 403 Forbidden with the exception's errorCode and message
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenException(
            ForbiddenException ex,
            HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDTO);
    }

    /**
     * Handlers service-layer validation failures. Distinct from Bean
     * Validation (see {@link #handleMethodArgumentNotValidException})
     * - this fires when the service detects a cross-field or
     * cross-resource invariant violation that the DTO layer cannot
     * express on its own.
     *
     * @param ex the validation exception, carrying its own errorCode and message
     * @param request current HTTP request (used to populate the path field)
     * @return 400 Bad Request with the exception's errorCode and message
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Handles business-rule conflicts - requests whose payload is
     * well-formed but conflicts with current domain state (e.g.
     * registering a username that's already taken, scheduling two
     * classes in the same room at the same time).
     *
     * <p>Distinct from {@link #handleDuplicateKeyException}: this is
     * the service-layer "explicit" pathway (the service checked and
     * threw), whereas {@code DuplicateKeyException} is the
     * database-layer fallback (a unique constraints fired without
     * service pre-check).
     *
     * @param ex a business-rule violation exception, carrying its own errorCode and message
     * @param request current HTTP request (used to populate the path field)
     * @return 409 Conflict with the exception's errorCode and message
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRuleViolationException(
            BusinessRuleViolationException ex,
            HttpServletRequest request) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    /**
     * Handles expired refresh-token attempts. Distinct from
     * {@link #handleInvalidRefreshToken} because expiry is a normal,
     * expected outcome (refresh tokens have finite TTL); invalidity is
     * an anomaly (tampered, revoked, or never-issued tokens).
     *
     * <p>Logged at {@code INFO} - expiry is routine; clients are
     * expected to log in again.
     *
     * @param ex the token-expired exception
     * @param request current HTTP request (used to populate the path field)
     * @return 401 Unauthorized with {@code REFRESH_TOKEN_EXPIRED} errorCode
     */
    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleRefreshTokenExpired(
            RefreshTokenExpiredException ex,
            HttpServletRequest request) {
        log.info("RefreshToken expired at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "REFRESH_TOKEN_EXPIRED",
                        ex.getMessage(),
                        LocalDateTime.now(),
                        request.getRequestURI()
                ));
    }

    /**
     * Handles invalid refresh-token attempts - tokens that are
     * tampered, revoked, or never-issued. Distinct from
     * {@link #handleRefreshTokenExpired} (which is routine expiry).
     *
     * <p>Logged at {@code INFO}, not {@code WARN} - invalid refresh
     * token are common (scanners, replay attempts, stale tokens after
     * password change) and shouldn't pollute warning-level
     * dashboards. Genuine security incidents are detected through
     * volume patterns, not individual log entries.
     *
     * @param ex the invalid-token exception
     * @param request current HTTP request (used to populate the path field)
     * @return 401 Unauthorized with {@code INVALID_REFRESH_TOKEN} errorCode
     */
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidRefreshToken(
            InvalidRefreshTokenException ex,
            HttpServletRequest request) {
        log.info("InvalidRefreshToken at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "INVALID_REFRESH_TOKEN",
                        ex.getMessage(),
                        LocalDateTime.now(),
                        request.getRequestURI()
                ));
    }

    /**
     * Handles requests to URLs that don't match any controller mapping.
     *
     * <p>Spring 6 / Spring Boot 3 changed default behaviour so that
     * requests to non-existent URLS throw {@link NoResourceFoundException}
     * instead of returning HTTP 404 directly. Without this dedicated
     * handler, the {@code Exception.class} catch-all below would map
     * these to HTTP 500 - an obvious bug, since "URL doesn't exist" is
     * a client problem, not a server error.
     *
     * <p> The external message is intentionally generic ("does not
     * exist") rather than echoing the requested path or framework
     * details - this avoids leaking implementation hints to malicious
     * scanners.
     *
     * @param ex the not-found exception thrown by Spring
     * @param request current HTTP request (used to populate the path field)
     * @return 404 Not Found with a generic resource-not-found body
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpServletRequest request) {
        log.warn("No resource found at {}", request.getRequestURI());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "RESOURCE_NOT_FOUND",
                "The requested resource does not exist",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    /**
     * Handles validation failures triggered by {@code @Valid} on
     * controller method parameters.
     *
     * <p>Spring throws {@link MethodArgumentNotValidException} when one
     * or more fields fail Bean Validation constraints (e.g.
     * {@code @NotBlank}, {@code Size}, {@code Pattern}). This handler
     * aggregates all field errors into a single human-readable message
     * and returns HTTP 400 Bad Request - the appropriate status for
     * client-side input errors.
     *
     * <p>For nested DTOs (e.g. {@code @Valid List<NestedDTO> items}),
     * Hibernate Validator records the array index in the field path,
     * producing messages like {@code "item[0].name: must not be blank"}.
     *
     * @param ex the validation exception thrown by Spring
     * @param request the current HTTP request (used to populate the path field)
     * @return a 400 Bad Request with aggregated field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request){

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "VALIDATION_FAILED",
                errorMessage,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Handles database unique-constraint violations.
     *
     * <p>When a write reaches the database with a value that violates a unique
     * constraint (e.g. creating a {@code dept} with a name that already exist),
     * Spring translates the underlying SQL error into
     * {@link org.springframework.dao.DuplicateKeyException}. Without this dedicated
     * handler, the {@code Exception.class} catch-all would map it to HTTP 500 - but
     * "you sent a value that conflicts with an existing resource" is a client
     * problem, not a server failure. The correct status is 409 Conflict.
     *
     * <p>The external message is intentionally generic ("a duplicate value
     * exists") rather than echoing the SQL exception text, which would leak
     * schema details (table names, index names) to clients. Services that want
     * a friendlier, field-specific message should catch
     * {@link org.springframework.dao.DataIntegrityViolationException} themselves
     * and rethrow as {@link BusinessRuleViolationException} - see
     * {@code EmpServiceImpl.register} for the pattern. This handler is the
     * safety net for code that doesn't.
     *
     * @param ex the duplicate-key exception thrown by Spring
     * @param request current HTTP request (used to populate the path field)
     * @return a 409 Conflict response with a generic duplicate-resource body
     */
    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateKeyException(
            DuplicateKeyException ex,
            HttpServletRequest request) {
        log.warn("Duplicate key violation at {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "DUPLICATE_RESOURCE",
                "A resource with the same unique value already exists",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    /**
     * Handles malformed or missing request bodies.
     *
     * <p>When Jackson cannot deserialize the request body — invalid JSON syntax
     * (e.g. {@code {"name":}}), incompatible types ({@code "age": "old"} where
     * Integer is expected), or a missing required body — Spring throws
     * {@link org.springframework.http.converter.HttpMessageNotReadableException}
     * before any controller method is invoked. Bean Validation never runs because
     * there's no valid object to validate.
     *
     * <p>Without this dedicated handler, malformed JSON would fall through to the
     * {@code Exception.class} catch-all and surface as 500 — misleading clients
     * into thinking the server is broken when in fact their request body is
     * unparseable. The correct status is 400 Bad Request.
     *
     * <p>The external message is intentionally generic. The underlying Jackson
     * parser message often contains line/column numbers and snippets of the
     * malformed input, which are useful for the server log but could echo back
     * sensitive content if clients accidentally include credentials in a botched
     * request. Clients should rely on their own JSON tooling rather than
     * server-side parse diagnostics.
     *
     * @param ex the unreadable-body exception thrown by Spring
     * @param request current HTTP request (used to populate the path field)
     * @return a 400 Bad Request response with a generic malformed-body message
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        log.warn("Malformed request body at {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "MALFORMED_REQUEST_BODY",
                "Request body is malformed or missing",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Last-restore handler for any exception that isn't matched by a
     * more specific handler above. Return HTTP 500 with a sanitised
     * generic message.
     *
     * <p>Every common client-side error should have its own dedicated
     * handler before this - if a category of {@code Exception} starts
     * frequently appearing in the logs under this handler's
     * {@code log.error} line, that's the signal to add a specific
     * handler with the appropriate 4xx status. Exising examples:
     * {@code NoResourceFoundException} (was 500, now 404),
     * {@code DuplicateKeyException} (was 500, now 409),
     * {@code HttpMessageNotReadableException} (was 500, now 400).
     *
     * <p>The response message is conservative - exposing internal
     * exception details could leak stack traces, paths, or
     * implementation hints the attackers.
     *
     * @param ex the uncaught exception
     * @param request current HTTP request (used to populate the path field)
     * @return Internal Server Error with a generic body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUncaughtException(
            Exception ex,
            HttpServletRequest request) {
        log.error("Uncaught Exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "INTERNAL_ERROR",
                "An unexpected error occurred. Please contact support.",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }
}
