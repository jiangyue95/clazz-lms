package com.yue.exception;

/**
 * Thrown when login fails due to invalid credentials —
 * either an unknown username or a wrong password.
 *
 * <p>Extends {@link UnauthorizedException} so it inherits the HTTP 401
 * mapping in the global exception handler. The distinction lets callers
 * (and logs) reason about the specific failure mode separately from
 * other 401 cases like missing/expired tokens.
 *
 * <p>The message exposed via this exception is intentionally vague
 * ("Invalid username or password") to prevent
 * <a href="https://owasp.org/www-community/attacks/Brute_force_attack">user enumeration attacks</a>.
 * Specific failure reasons should be logged server-side, never returned to clients.
 */
public class InvalidCredentialsException extends UnauthorizedException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
