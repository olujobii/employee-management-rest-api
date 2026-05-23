package com.olujobii.employeemanagementsystem.exception;

import org.springframework.http.HttpStatusCode;

public class DuplicateEmailException extends EmployeeException {

    public DuplicateEmailException(String message, HttpStatusCode status) {
        super(message, status);
    }
}
