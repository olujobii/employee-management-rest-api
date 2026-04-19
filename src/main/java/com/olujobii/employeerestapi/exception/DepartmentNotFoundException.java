package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class DepartmentNotFoundException  extends DepartmentException {
    public DepartmentNotFoundException(String message, HttpStatus httpStatus) {
        super(message,httpStatus);
    }
}
