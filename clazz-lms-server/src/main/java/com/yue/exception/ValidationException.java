package com.yue.exception;

public class ValidationException extends BaseException{

    public ValidationException(String message) {
        super("VALIDATION_ERROR_FAILED", message);
    }
}
