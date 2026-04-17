package com.olujobii.employeerestapi.exception;

import com.olujobii.employeerestapi.employee.dto.EmployeeErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleDuplicateEmailException(DuplicateEmailException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new EmployeeErrorResponseDto(HttpStatus.CONFLICT,ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(InsufficientSalaryException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleInvalidSalaryException(InsufficientSalaryException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new EmployeeErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleEmployeeNotFoundException(EmployeeNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new EmployeeErrorResponseDto(HttpStatus.NOT_FOUND,ex.getMessage(),LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidActiveFieldException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleInvalidActiveFieldException(InvalidActiveFieldException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new EmployeeErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(),LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidPatchRequestBodyException.class)
    public ResponseEntity<EmployeeErrorResponseDto> invalidPatchRequestBody(InvalidPatchRequestBodyException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new EmployeeErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(),LocalDateTime.now())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new EmployeeErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<com.olujobii.employeerestapi.department.dto.EmployeeErrorResponseDto> handleEmployeeNotFoundException(DepartmentNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new com.olujobii.employeerestapi.department.dto.EmployeeErrorResponseDto(HttpStatus.NOT_FOUND,
                ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(DuplicateDepartmentException.class)
    public ResponseEntity<com.olujobii.employeerestapi.department.dto.EmployeeErrorResponseDto> handleDuplicateDepartmentException(DuplicateDepartmentException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new com.olujobii.employeerestapi.department.dto.EmployeeErrorResponseDto(HttpStatus.CONFLICT,
                ex.getMessage(), LocalDateTime.now()));
    }
}
