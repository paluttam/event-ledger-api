package com.example.eventledger.service;

import com.example.eventledger.dto.BalanceResponse;
import com.example.eventledger.dto.EventRequest;
import com.example.eventledger.entity.Event;
import com.example.eventledger.entity.EventType;
import com.example.eventledger.exception.ResourceNotFoundException;
import com.example.eventledger.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;

    @Transactional
    public Event createEvent(EventRequest request) {

        return repository.findById(request.getEventId())
                .orElseGet(() -> {

                    Event event = Event.builder()
                            .eventId(request.getEventId())
                            .accountId(request.getAccountId())
                            .type(request.getType())
                            .amount(request.getAmount())
                            .currency(request.getCurrency())
                            .eventTimestamp(request.getEventTimestamp())
                            .metadata(request.getMetadata())
                            .build();

                    return repository.save(event);
                });
    }

    public Event getEvent(String id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found with id: " + id));
    }

    public List<Event> getEventsByAccount(String accountId) {

        return repository.findByAccountIdOrderByEventTimestampAsc(accountId);
    }

    public BalanceResponse getBalance(String accountId) {

        List<Event> events = repository
                .findByAccountIdOrderByEventTimestampAsc(accountId);

        BigDecimal balance = events.stream()
                .map(event -> {
                    if (event.getType() == EventType.CREDIT) {
                        return event.getAmount();
                    } else {
                        return event.getAmount().negate();
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BalanceResponse(accountId, balance);
    }
}