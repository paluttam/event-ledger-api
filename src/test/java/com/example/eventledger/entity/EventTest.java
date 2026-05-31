package com.example.eventledger.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void testBuilderAndGetters() {

        Instant now = Instant.now();

        Event event = Event.builder()
                .eventId("evt-1")
                .accountId("acct-1")
                .type(EventType.CREDIT)
                .amount(BigDecimal.TEN)
                .currency("USD")
                .eventTimestamp(now)
                .metadata("meta")
                .build();

        assertEquals("evt-1", event.getEventId());
        assertEquals("acct-1", event.getAccountId());
        assertEquals(EventType.CREDIT, event.getType());
        assertEquals(BigDecimal.TEN, event.getAmount());
        assertEquals("USD", event.getCurrency());
        assertEquals(now, event.getEventTimestamp());
        assertEquals("meta", event.getMetadata());
    }

    @Test
    void testSetters() {

        Instant now = Instant.now();

        Event event = new Event();

        event.setEventId("evt-2");
        event.setAccountId("acct-2");
        event.setType(EventType.DEBIT);
        event.setAmount(BigDecimal.ONE);
        event.setCurrency("EUR");
        event.setEventTimestamp(now);
        event.setMetadata("metadata");

        assertEquals("evt-2", event.getEventId());
        assertEquals("acct-2", event.getAccountId());
        assertEquals(EventType.DEBIT, event.getType());
        assertEquals(BigDecimal.ONE, event.getAmount());
        assertEquals("EUR", event.getCurrency());
        assertEquals(now, event.getEventTimestamp());
        assertEquals("metadata", event.getMetadata());
    }
}