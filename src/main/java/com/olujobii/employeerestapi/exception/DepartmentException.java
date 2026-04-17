package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class DepartmentException extends RuntimeException {
    private final HttpStatus httpStatus;

    public DepartmentException(String message, HttpStatus httpStatus) {
        super("Error Occurred: "+message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
