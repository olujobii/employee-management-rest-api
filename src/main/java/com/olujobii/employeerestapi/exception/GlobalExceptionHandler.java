package com.olujobii.employeerestapi.exception;

import com.olujobii.employeerestapi.department.dto.response.DepartmentErrorResponseDto;
import com.olujobii.employeerestapi.employee.dto.response.EmployeeErrorResponseDto;
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
        return ResponseEntity.status(ex.getHttpStatus()).body(
                new EmployeeErrorResponseDto(ex.getHttpStatus(),ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleEmployeeException(EmployeeException ex){
        return ResponseEntity.status(ex.getHttpStatus()).body(
                new EmployeeErrorResponseDto(ex.getHttpStatus(),ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleEmployeeNotFoundException(EmployeeNotFoundException ex){
        return ResponseEntity.status(ex.getHttpStatus()).body(
                new EmployeeErrorResponseDto(ex.getHttpStatus(),ex.getMessage(),LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidEmployeePatchRequestBodyException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleInvalidPatchRequestBody(InvalidEmployeePatchRequestBodyException ex){
        return ResponseEntity.status(ex.getHttpStatus()).body(
                new EmployeeErrorResponseDto(ex.getHttpStatus(),ex.getMessage(),LocalDateTime.now())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new EmployeeErrorResponseDto(HttpStatus.BAD_REQUEST,ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<EmployeeErrorResponseDto> handleEmployeeNotFoundException(DepartmentNotFoundException ex){
        return ResponseEntity.status(ex.getHttpStatus()).body(new EmployeeErrorResponseDto(ex.getHttpStatus(),
                ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(DuplicateDepartmentException.class)
    public ResponseEntity<DepartmentErrorResponseDto> handleDuplicateDepartmentException(DuplicateDepartmentException ex){
        return ResponseEntity.status(ex.getHttpStatus()).body(new DepartmentErrorResponseDto(ex.getHttpStatus(),
                ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<DepartmentErrorResponseDto> handleDepartmentNotFoundException(DepartmentException ex){
        return ResponseEntity.status(ex.getHttpStatus()).body(new DepartmentErrorResponseDto(ex.getHttpStatus(),
                ex.getMessage(),LocalDateTime.now()));
    }
}
