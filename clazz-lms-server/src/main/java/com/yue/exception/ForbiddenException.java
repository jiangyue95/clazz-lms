package com.yue.exception;

public class ForbiddenException extends BaseException{

    public ForbiddenException(String message) {
        super("FORBIDDEN", message);
    }
}
