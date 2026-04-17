package com.olujobii.employeerestapi.exception;

public class InsufficientSalaryException extends RuntimeException {
    public InsufficientSalaryException(String message) {
        super(message);
    }
}
