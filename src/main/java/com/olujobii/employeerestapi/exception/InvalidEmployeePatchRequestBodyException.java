package com.olujobii.employeerestapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidEmployeePatchRequestBodyException extends EmployeeException {
    public InvalidEmployeePatchRequestBodyException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
