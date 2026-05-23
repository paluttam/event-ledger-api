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
import com.example.eventledger.dto.EventRequest;
import com.example.eventledger.entity.Event;
import com.example.eventledger.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventController {
	
    private final EventService service;

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(
            @Valid @RequestBody EventRequest request) {

        boolean exists = false;

        try {
            service.getEvent(request.getEventId());
            exists = true;
        } catch (Exception ignored) {
        }

        Event event = service.createEvent(request);

        if (exists) {
            return ResponseEntity.ok(event);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable String id) {

        return ResponseEntity.ok(service.getEvent(id));
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEventsByAccount(
            @RequestParam String account) {

        return ResponseEntity.ok(
                service.getEventsByAccount(account));
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                service.getBalance(accountId));
    }
}