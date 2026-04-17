package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class DepartmentNotFoundException extends DepartmentException {
    public DepartmentNotFoundException(Long id, HttpStatus httpStatus) {
        super("Department does not exist for ID: "+id,httpStatus);
    }
}
