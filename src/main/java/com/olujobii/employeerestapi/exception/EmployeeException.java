package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class EmployeeException extends RuntimeException {
    private final HttpStatus httpStatus;

    public EmployeeException(String message, HttpStatus httpStatus) {
        super("Error Occurred: "+message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
