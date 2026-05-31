package com.example.eventledger.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {

        ResourceNotFoundException exception =
                new ResourceNotFoundException(
                        "event not found");

        assertEquals(
                "event not found",
                exception.getMessage());
    }
}