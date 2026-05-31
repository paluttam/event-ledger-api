package com.example.eventledger.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventledger.dto.BalanceResponse;
import com.example.eventledger.dto.EventCreationResult;
import com.example.eventledger.dto.EventRequest;
import com.example.eventledger.entity.Event;
import com.example.eventledger.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Event Ledger API", description = "Financial transaction event ledger operations")
public class EventController {

    private final EventService service;

    @PostMapping("/events")
    @Operation(
            summary = "Create Event",
            description = "Creates a new event. If the event already exists, returns the original event without affecting balance."
    )
    public ResponseEntity<Event> createEvent(
            @Valid @RequestBody EventRequest request) {

        EventCreationResult result = service.createEvent(request);

        if (result.isDuplicate()) {
            return ResponseEntity.ok(result.getEvent());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result.getEvent());
    }

    @GetMapping("/events/{id}")
    @Operation(
            summary = "Get Event By Id",
            description = "Retrieves a single event using its eventId"
    )
    public ResponseEntity<Event> getEvent(
            @PathVariable String id) {

        return ResponseEntity.ok(
                service.getEvent(id));
    }

    @GetMapping("/events")
    @Operation(
            summary = "Get Events By Account",
            description = "Returns all events for an account ordered by event timestamp"
    )
    public ResponseEntity<List<Event>> getEventsByAccount(
            @RequestParam String account) {

        return ResponseEntity.ok(
                service.getEventsByAccount(account));
    }

    @GetMapping("/accounts/{accountId}/balance")
    @Operation(
            summary = "Get Account Balance",
            description = "Returns net balance = Credits - Debits"
    )
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                service.getBalance(accountId));
    }
}