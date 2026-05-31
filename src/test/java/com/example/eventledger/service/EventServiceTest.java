package com.example.eventledger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.eventledger.dto.BalanceResponse;
import com.example.eventledger.dto.EventCreationResult;
import com.example.eventledger.dto.EventRequest;
import com.example.eventledger.entity.Event;
import com.example.eventledger.entity.EventType;
import com.example.eventledger.exception.ResourceNotFoundException;
import com.example.eventledger.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventService service;

    private Event event;

    @BeforeEach
    void setup() {

        event = Event.builder()
                .eventId("evt-1")
                .accountId("acct-1")
                .type(EventType.CREDIT)
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .eventTimestamp(Instant.now())
                .metadata("test")
                .build();
    }

    @Test
    void createEvent_newEvent() {

        EventRequest request = new EventRequest();
        request.setEventId("evt-1");
        request.setAccountId("acct-1");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        when(repository.saveAndFlush(any(Event.class)))
                .thenReturn(event);

        EventCreationResult result =
                service.createEvent(request);

        assertFalse(result.isDuplicate());
        assertEquals(event, result.getEvent());
    }

    @Test
    void createEvent_duplicateEvent() {

        EventRequest request = new EventRequest();
        request.setEventId("evt-1");

        when(repository.saveAndFlush(any(Event.class)))
                .thenThrow(DataIntegrityViolationException.class);

        when(repository.findById("evt-1"))
                .thenReturn(Optional.of(event));

        EventCreationResult result =
                service.createEvent(request);

        assertTrue(result.isDuplicate());
        assertEquals(event, result.getEvent());
    }

    @Test
    void createEvent_duplicateButMissingRecord() {

        EventRequest request = new EventRequest();
        request.setEventId("evt-1");

        when(repository.saveAndFlush(any(Event.class)))
                .thenThrow(DataIntegrityViolationException.class);

        when(repository.findById("evt-1"))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> service.createEvent(request));
    }

    @Test
    void getEvent_success() {

        when(repository.findById("evt-1"))
                .thenReturn(Optional.of(event));

        Event result = service.getEvent("evt-1");

        assertEquals(event, result);
    }

    @Test
    void getEvent_notFound() {

        when(repository.findById("evt-1"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getEvent("evt-1"));
    }

    @Test
    void getEventsByAccount() {

        when(repository.findByAccountIdOrderByEventTimestampAsc("acct-1"))
                .thenReturn(List.of(event));

        List<Event> result =
                service.getEventsByAccount("acct-1");

        assertEquals(1, result.size());
    }

    @Test
    void getBalance_creditAndDebit() {

        Event credit = Event.builder()
                .type(EventType.CREDIT)
                .amount(BigDecimal.valueOf(100))
                .build();

        Event debit = Event.builder()
                .type(EventType.DEBIT)
                .amount(BigDecimal.valueOf(30))
                .build();

        when(repository.findByAccountId("acct-1"))
                .thenReturn(List.of(credit, debit));

        BalanceResponse response =
                service.getBalance("acct-1");

        assertEquals(
                BigDecimal.valueOf(70),
                response.getBalance());
    }
}