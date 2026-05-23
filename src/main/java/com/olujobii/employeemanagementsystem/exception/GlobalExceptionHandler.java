package com.olujobii.employeemanagementsystem.exception;

import com.olujobii.employeemanagementsystem.dto.response.APIErrorResponseDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateDepartmentException.class)
    public ResponseEntity<APIErrorResponseDTO> handleDuplicateDepartmentException(DuplicateDepartmentException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now(),
                HttpStatusCode.valueOf(ex.getStatus().value()));

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(InvalidActiveStateException.class)
    public ResponseEntity<APIErrorResponseDTO> handleInvalidActiveStateException(InvalidActiveStateException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now(),
                HttpStatusCode.valueOf(ex.getStatus().value()));

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now(),
                HttpStatusCode.valueOf(ex.getStatus().value()));

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<APIErrorResponseDTO> handleEmployeeException(EmployeeException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now(),
                HttpStatusCode.valueOf(ex.getStatus().value()));

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<APIErrorResponseDTO> handleDepartmentException(DepartmentException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now(),
                HttpStatusCode.valueOf(ex.getStatus().value()));

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}
