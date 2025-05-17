package com.liptsoft.transactionservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transaction {
    private String transactionId;
    private String customerId;
    private String accountId;
    private String transactionType;
    private double amount;
    private LocalDateTime dateTime;
}
