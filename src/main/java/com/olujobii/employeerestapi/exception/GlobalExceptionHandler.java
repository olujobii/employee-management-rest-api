package com.olujobii.employeerestapi.exception;

import com.olujobii.employeerestapi.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDto> duplicateEmail(DuplicateEmailException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponseDto(HttpStatus.CONFLICT,ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(InvalidSalaryException.class)
    public ResponseEntity<ErrorResponseDto> invalidSalary(InvalidSalaryException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> employeeNotFound(EmployeeNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto(HttpStatus.NOT_FOUND,ex.getMessage(),LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidActiveFieldException.class)
    public ResponseEntity<ErrorResponseDto> invalidActiveField(InvalidActiveFieldException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(),LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidPatchRequestBodyException.class)
    public ResponseEntity<ErrorResponseDto> invalidPatchRequestBody(InvalidPatchRequestBodyException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(),LocalDateTime.now())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> constraintViolation(ConstraintViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(), LocalDateTime.now())
        );
    }
}
