package com.example.eventledger.dto;

import com.example.eventledger.entity.Event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventCreationResult {

    private Event event;
    private boolean duplicate;
}