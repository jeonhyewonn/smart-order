package com.example.smartorder.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ExceptionResponse {
    private final String code;
    private final String message;
    private final Map<String, String> field = new HashMap<>();

    public ExceptionResponse(HttpStatus status, Exception e) {
        this.code = status.name();
        this.message = e.getClass().getSimpleName();
    }


    public void addCauseField(String fieldName, String errorMessage) {
        this.field.put(fieldName, errorMessage);
    }
}
