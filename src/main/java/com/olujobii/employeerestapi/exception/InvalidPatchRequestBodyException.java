package com.olujobii.employeerestapi.exception;

public class InvalidPatchRequestBodyException extends RuntimeException {
    public InvalidPatchRequestBodyException(String message) {
        super(message);
    }
}
