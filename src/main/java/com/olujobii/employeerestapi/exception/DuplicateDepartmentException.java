package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class DuplicateDepartmentException extends DepartmentException {

    public DuplicateDepartmentException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
