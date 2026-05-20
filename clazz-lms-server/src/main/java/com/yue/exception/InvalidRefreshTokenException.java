package com.yue.exception;

/**
 * Thrown when a refresh token is rejected for any reason other than
 * expiry: signature failure, missing from Redis, already revoked, or
 * the underlying employee no longer exists.
 *
 * <p>All these cases collapse to a single error code so attackers
 * can't probe with specific check failed (similar to login user-
 * enumeration defence).
 */
public class InvalidRefreshTokenException extends BaseException {

    public InvalidRefreshTokenException(String message) {
        super("INVALID_REFRESH_TOKEN", message);
    }
}
