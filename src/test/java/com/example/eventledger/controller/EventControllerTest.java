package com.example.eventledger.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    void testGetMissingEvent() throws Exception {

        mockMvc.perform(get("/events/not-found"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testInvalidEventType() throws Exception {

        String json = """
                {
                  "eventId":"1",
                  "accountId":"acct",
                  "type":"INVALID",
                  "amount":100,
                  "currency":"USD",
                  "eventTimestamp":"2026-05-15T14:02:11Z"
                }
                """;

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testValidationMessages() throws Exception {

        String json = """
                {
                  "eventId":"",
                  "accountId":"",
                  "amount":0
                }
                """;

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testGetBalance() throws Exception {

        EventRequest request = new EventRequest();

        request.setEventId("evt-bal");
        request.setAccountId("acct-balance");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/accounts/acct-balance/balance"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEventsByAccount() throws Exception {

        EventRequest request = new EventRequest();

        request.setEventId("evt-list");
        request.setAccountId("acct-list");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/events")
                        .param("account", "acct-list"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEventById() throws Exception {

        EventRequest request = new EventRequest();

        request.setEventId("evt-get");
        request.setAccountId("acct-get");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(500));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/events/evt-get"))
                .andExpect(status().isOk());
    }
}