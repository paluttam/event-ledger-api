package com.example.eventledger.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.eventledger.dto.EventRequest;
import com.example.eventledger.entity.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testCreateEvent() throws Exception {

        EventRequest request = new EventRequest();

        request.setEventId("evt-001");
        request.setAccountId("acct-123");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDuplicateEvent() throws Exception {

        EventRequest request = new EventRequest();

        request.setEventId("evt-002");
        request.setAccountId("acct-123");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(200));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testValidation() throws Exception {

        String invalidJson = """
                {
                    "eventId":"",
                    "amount":-10
                }
                """;

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}