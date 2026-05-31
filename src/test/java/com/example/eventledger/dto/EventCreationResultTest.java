package com.example.eventledger.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.eventledger.entity.Event;

class EventCreationResultTest {

    @Test
    void testConstructorAndGetter() {

        Event event = new Event();

        EventCreationResult result =
                new EventCreationResult(event, true);

        assertEquals(
                event,
                result.getEvent());

        assertTrue(
                result.isDuplicate());
    }
}