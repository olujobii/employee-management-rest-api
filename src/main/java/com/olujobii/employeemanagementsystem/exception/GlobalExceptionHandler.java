package com.olujobii.employeemanagementsystem.exception;

import com.olujobii.employeemanagementsystem.dto.response.APIErrorResponseDTO;
import com.olujobii.employeemanagementsystem.dto.response.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateDepartmentException.class)
    public ResponseEntity<ResponseWrapper<APIErrorResponseDTO>> handleDuplicateDepartmentException(DuplicateDepartmentException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now());
        ResponseWrapper<APIErrorResponseDTO> response = ResponseWrapper.<APIErrorResponseDTO>builder()
                                                                        .data(errorResponse)
                                                                        .statusCode(ex.getStatus())
                                                                        .message(ex.getMessage())
                                                                        .build();

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidActiveStateException.class)
    public ResponseEntity<ResponseWrapper<APIErrorResponseDTO>> handleInvalidActiveStateException(InvalidActiveStateException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now());
        ResponseWrapper<APIErrorResponseDTO> response = ResponseWrapper.<APIErrorResponseDTO>builder()
                .data(errorResponse)
                .statusCode(ex.getStatus())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<APIErrorResponseDTO>> handleResourceNotFoundException(ResourceNotFoundException ex){
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(ex.getMessage(), LocalDateTime.now());
        ResponseWrapper<APIErrorResponseDTO> response = ResponseWrapper.<APIErrorResponseDTO>builder()
                .data(errorResponse)
                .statusCode(ex.getStatus())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}
