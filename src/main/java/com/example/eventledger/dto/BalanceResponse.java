package com.example.eventledger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceResponse {

    private String accountId;
    private BigDecimal balance;
}