package com.olujobii.employeerestapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DepartmentException extends RuntimeException {
    private final HttpStatus httpStatus;

    public DepartmentException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
