package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class EmployeeNotFoundException extends EmployeeException {
    public EmployeeNotFoundException(Long id, HttpStatus httpStatus) {
        super("Employee does not exist for ID: "+id, httpStatus);
    }
}
