package com.olujobii.employeemanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class InvalidActiveStateException extends RuntimeException {
    private final HttpStatusCode status;

    public InvalidActiveStateException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }
}
