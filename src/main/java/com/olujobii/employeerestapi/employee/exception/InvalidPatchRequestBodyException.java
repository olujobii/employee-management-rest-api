package com.olujobii.employeerestapi.employee.exception;

public class InvalidPatchRequestBodyException extends RuntimeException {
    public InvalidPatchRequestBodyException(String message) {
        super(message);
    }
}
