package com.example.eventledger.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EventTypeTest {

    @Test
    void testEnumValues() {

        assertEquals(
                EventType.CREDIT,
                EventType.valueOf("CREDIT"));

        assertEquals(
                EventType.DEBIT,
                EventType.valueOf("DEBIT"));
    }
}