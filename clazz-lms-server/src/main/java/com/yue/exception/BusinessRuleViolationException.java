package com.yue.exception;

public class BusinessRuleViolationException extends BaseException{

    public BusinessRuleViolationException(String message) {
        super("BUSINESS_RULE_VIOLATION", message);
    }
}
