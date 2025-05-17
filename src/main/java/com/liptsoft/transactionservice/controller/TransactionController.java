package com.liptsoft.transactionservice.controller;

import com.liptsoft.transactionservice.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TransactionController {

    @GetMapping("/api/transactions")
    ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);
}
