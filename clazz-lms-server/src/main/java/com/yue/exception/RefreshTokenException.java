package com.yue.exception;

public class RefreshTokenException extends BaseException {
    public RefreshTokenException(String message) {
        super("REFRESH_TOKEN_ERROR", message);
    }

    public RefreshTokenException(String errorCode, String message) {
        super(errorCode, message);
    }
}
