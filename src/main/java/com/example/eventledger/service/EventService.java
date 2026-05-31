package com.example.eventledger.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eventledger.dto.BalanceResponse;
import com.example.eventledger.dto.EventCreationResult;
import com.example.eventledger.dto.EventRequest;
import com.example.eventledger.entity.Event;
import com.example.eventledger.entity.EventType;
import com.example.eventledger.exception.ResourceNotFoundException;
import com.example.eventledger.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;

    @Transactional
    public EventCreationResult createEvent(EventRequest request) {

        try {

            Event event = Event.builder()
                    .eventId(request.getEventId())
                    .accountId(request.getAccountId())
                    .type(request.getType())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .eventTimestamp(request.getEventTimestamp())
                    .metadata(request.getMetadata())
                    .build();

            Event saved = repository.saveAndFlush(event);

            return new EventCreationResult(saved, false);

        } catch (DataIntegrityViolationException ex) {

            Event existing = repository.findById(request.getEventId())
                    .orElseThrow();

            return new EventCreationResult(existing, true);
        }
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

        BigDecimal balance =
                repository.findByAccountId(accountId)
                        .stream()
                        .map(event ->
                                event.getType() == EventType.CREDIT
                                        ? event.getAmount()
                                        : event.getAmount().negate())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BalanceResponse(
                accountId,
                balance);
    }
}