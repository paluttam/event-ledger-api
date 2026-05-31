package com.example.eventledger.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.eventledger.entity.Event;
import com.example.eventledger.entity.EventType;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository repository;

    @Test
    void shouldReturnEventsOrderedByTimestamp() {

        Event later = Event.builder()
                .eventId("evt-1")
                .accountId("acct-1")
                .type(EventType.CREDIT)
                .amount(BigDecimal.TEN)
                .currency("USD")
                .eventTimestamp(
                        Instant.parse("2026-05-15T10:00:00Z"))
                .metadata("later")
                .build();

        Event earlier = Event.builder()
                .eventId("evt-2")
                .accountId("acct-1")
                .type(EventType.CREDIT)
                .amount(BigDecimal.TEN)
                .currency("USD")
                .eventTimestamp(
                        Instant.parse("2026-05-15T09:00:00Z"))
                .metadata("earlier")
                .build();

        repository.save(later);
        repository.save(earlier);

        List<Event> events =
                repository.findByAccountIdOrderByEventTimestampAsc(
                        "acct-1");

        assertEquals(2, events.size());

        assertEquals(
                "evt-2",
                events.get(0).getEventId());

        assertEquals(
                "evt-1",
                events.get(1).getEventId());
    }
}