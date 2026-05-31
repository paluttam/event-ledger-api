package com.example.eventledger.repository;

import com.example.eventledger.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByAccountIdOrderByEventTimestampAsc(String accountId);

    List<Event> findByAccountId(String accountId);
}