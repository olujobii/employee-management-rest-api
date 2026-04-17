package com.olujobii.employeerestapi.exception;

public class InvalidActiveFieldException extends RuntimeException {
    public InvalidActiveFieldException(String message) {
        super(message);
    }
}
