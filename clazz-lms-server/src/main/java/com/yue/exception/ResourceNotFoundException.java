package com.yue.exception;

public class ResourceNotFoundException extends BaseException{

    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message);
    }
}
