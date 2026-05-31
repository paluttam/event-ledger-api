package com.example.eventledger.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.example.eventledger.dto.ErrorResponse;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void handleNotFound() {

        ResponseEntity<ErrorResponse> response =
                handler.handleNotFound(
                        new ResourceNotFoundException("not found"));

        assertEquals(
                404,
                response.getStatusCode().value());
    }

    @Test
    void handleInvalidJson() {

        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidJson(
                        new HttpMessageNotReadableException("bad request"));

        assertEquals(
                400,
                response.getStatusCode().value());
    }
}