package com.olujobii.employeemanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class EmployeeException extends RuntimeException {
    private final HttpStatusCode status;

    public EmployeeException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }
}
