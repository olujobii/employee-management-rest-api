package com.olujobii.employeemanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class DuplicateDepartmentException extends RuntimeException {
    private final HttpStatusCode status;
    public DuplicateDepartmentException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }
}
