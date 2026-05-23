package com.olujobii.employeemanagementsystem.dto.response;

import lombok.Builder;
import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

@Getter
@ToString
@RequiredArgsConstructor
@Builder
public class ResponseWrapper <T>{
    private final T data;
    private final String message;
    private final HttpStatusCode statusCode;
}
