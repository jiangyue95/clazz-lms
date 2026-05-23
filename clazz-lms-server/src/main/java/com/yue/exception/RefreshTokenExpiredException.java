package com.yue.exception;

/**
 * Thrown when a refresh token has expired (JWT exp claim past now).
 *
 * <p>Distinct from {@link InvalidRefreshTokenException} so clients can
 * react specifically - an expired refresh token means the user must
 * log in again, whereas other invalid-token cases collapse to a single
 * generic error to avoid leaking which validation step failed.
 */

public class RefreshTokenExpiredException extends BaseException {

    public RefreshTokenExpiredException(String message) {
        super("REFRESH_TOKEN_EXPIRED", message);
    }
}
