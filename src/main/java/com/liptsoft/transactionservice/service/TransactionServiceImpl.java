package com.liptsoft.transactionservice.service;

import com.liptsoft.transactionservice.model.Transaction;
import com.liptsoft.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getTransactions(String accountId, String transactionType, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        try {
            return transactionRepository.getTransactions(accountId, transactionType, startDate, endDate, page, size);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching transactions: " + e.getMessage(), e);
        }
    }
}
