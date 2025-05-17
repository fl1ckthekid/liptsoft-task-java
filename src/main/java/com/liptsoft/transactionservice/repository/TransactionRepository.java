package com.liptsoft.transactionservice.repository;

import com.liptsoft.transactionservice.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> getTransactions(String accountId, String transactionType, LocalDateTime startDate, LocalDateTime endDate, int page, int size);
}
