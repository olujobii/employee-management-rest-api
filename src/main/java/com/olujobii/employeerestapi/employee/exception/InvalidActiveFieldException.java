package com.olujobii.employeerestapi.employee.exception;

public class InvalidActiveFieldException extends RuntimeException {
    public InvalidActiveFieldException(String message) {
        super(message);
    }
}
