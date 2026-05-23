package com.olujobii.employeemanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class DepartmentException extends RuntimeException {
    private final HttpStatusCode status;

    public DepartmentException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }
}
