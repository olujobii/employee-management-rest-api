package com.olujobii.employeemanagementsystem.exception;

import org.springframework.http.HttpStatusCode;

public class DuplicateDepartmentException extends DepartmentException {

    public DuplicateDepartmentException(String message, HttpStatusCode status) {
        super(message, status);
    }
}
