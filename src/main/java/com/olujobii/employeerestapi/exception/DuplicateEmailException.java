package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends EmployeeException {
    public DuplicateEmailException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
