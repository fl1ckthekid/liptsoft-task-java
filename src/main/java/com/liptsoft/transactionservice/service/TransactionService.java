package com.liptsoft.transactionservice.service;

import com.liptsoft.transactionservice.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactions(String accountId, String transactionType, LocalDateTime startDate, LocalDateTime endDate, int page, int size);
}
