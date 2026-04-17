package com.olujobii.employeerestapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmployeeException extends RuntimeException {
    private final HttpStatus httpStatus;

    public EmployeeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
