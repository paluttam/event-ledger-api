package com.example.eventledger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    private String eventId;

    private String accountId;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private BigDecimal amount;

    private String currency;

    private Instant eventTimestamp;

    @Lob
    private String metadata;
}