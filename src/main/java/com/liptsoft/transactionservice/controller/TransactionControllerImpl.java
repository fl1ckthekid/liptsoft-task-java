package com.liptsoft.transactionservice.controller;

import com.liptsoft.transactionservice.model.Transaction;
import com.liptsoft.transactionservice.service.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionControllerImpl implements TransactionController {

    private final TransactionServiceImpl transactionServiceImpl;

    public TransactionControllerImpl(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            LocalDateTime start = startDate == null ? LocalDateTime.MIN : LocalDateTime.parse(startDate);
            LocalDateTime end = endDate == null ? LocalDateTime.MAX : LocalDateTime.parse(endDate);

            List<Transaction> transactions = transactionServiceImpl.getTransactions(accountId, transactionType, start, end, page, size);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameter type: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
