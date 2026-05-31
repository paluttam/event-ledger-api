package com.example.eventledger.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;

class OpenApiConfigTest {

    @Test
    void openApiBeanCreated() {

        OpenApiConfig config =
                new OpenApiConfig();

        OpenAPI api =
                config.eventLedgerApi();

        assertEquals(
                "Event Ledger API",
                api.getInfo().getTitle());

        assertEquals(
                "1.0",
                api.getInfo().getVersion());

        assertEquals(
                "Financial Event Ledger Service",
                api.getInfo().getDescription());
    }
}