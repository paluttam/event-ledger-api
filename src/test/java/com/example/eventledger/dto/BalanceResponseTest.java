package com.example.eventledger.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class BalanceResponseTest {

    @Test
    void testBalanceResponse() {

        BalanceResponse response =
                new BalanceResponse(
                        "acct-1",
                        BigDecimal.TEN);

        assertEquals(
                "acct-1",
                response.getAccountId());

        assertEquals(
                BigDecimal.TEN,
                response.getBalance());
    }
}