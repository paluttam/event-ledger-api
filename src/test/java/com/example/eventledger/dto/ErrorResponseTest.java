package com.example.eventledger.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void testErrorResponse() {

        LocalDateTime now =
                LocalDateTime.now();

        ErrorResponse response =
                new ErrorResponse(
                        now,
                        400,
                        "error");

        assertEquals(now,
                response.getTimestamp());

        assertEquals(
                400,
                response.getStatus());

        assertEquals(
                "error",
                response.getError());
    }
}