package com.example.eventledger.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.example.eventledger.entity.EventType;

class EventRequestTest {

    @Test
    void testEventRequest() {

        EventRequest request =
                new EventRequest();

        request.setEventId("evt-1");
        request.setAccountId("acct-1");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.TEN);
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());
        request.setMetadata("meta");

        assertEquals("evt-1",
                request.getEventId());

        assertEquals("acct-1",
                request.getAccountId());

        assertEquals(EventType.CREDIT,
                request.getType());

        assertEquals(BigDecimal.TEN,
                request.getAmount());

        assertEquals("USD",
                request.getCurrency());

        assertEquals("meta",
                request.getMetadata());
    }
}