package com.liptsoft.transactionservice;

import com.liptsoft.transactionservice.model.Transaction;
import com.liptsoft.transactionservice.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceImplApplicationTests {

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactions = transactionServiceImpl.getTransactions(null, null, LocalDateTime.MIN, LocalDateTime.MAX, 0, 20);
        assertNotNull(transactions);
        assertTrue(transactions.size() <= 20);
    }

    @Test
    void testGetTransactionsByAccount() {
        List<Transaction> transactions = transactionServiceImpl.getTransactions("1561887", null, LocalDateTime.MIN, LocalDateTime.MAX, 0, 20);
        assertNotNull(transactions);
        assertTrue(transactions.stream().allMatch(t -> t.getAccountId().equals("1561887")));
    }

    @Test
    void testGetTransactionsByDateRange() {
        LocalDateTime start = LocalDateTime.of(2022, 12, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 12, 31, 23, 59);
        List<Transaction> transactions = transactionServiceImpl.getTransactions(null, null, start, end, 0, 20);
        assertNotNull(transactions);
        assertTrue(transactions.stream().allMatch(t -> !t.getDateTime().isBefore(start) && !t.getDateTime().isAfter(end)));
    }

    @Test
    void testPagination() {
        List<Transaction> firstPage = transactionServiceImpl.getTransactions(null, null, LocalDateTime.MIN, LocalDateTime.MAX, 0, 10);
        List<Transaction> secondPage = transactionServiceImpl.getTransactions(null, null, LocalDateTime.MIN, LocalDateTime.MAX, 1, 20);
        assertNotNull(firstPage);
        assertNotNull(secondPage);
        assertEquals(10, firstPage.size());
        assertEquals(20, secondPage.size());
        assertNotEquals(firstPage.getFirst().getTransactionId(), secondPage.getFirst().getTransactionId());
    }

    @Test
    void testGetTransactionsByTransactionTypeIncome() {
        List<Transaction> transactions = transactionServiceImpl.getTransactions(null, "income", LocalDateTime.MIN, LocalDateTime.MAX, 0, 20);
        assertNotNull(transactions);
        assertTrue(transactions.stream().allMatch(t -> t.getTransactionType().equals("income")));
    }

    @Test
    void testGetTransactionsByTransactionTypeOutcome() {
        List<Transaction> transactions = transactionServiceImpl.getTransactions(null, "outcome", LocalDateTime.MIN, LocalDateTime.MAX, 0, 20);
        assertNotNull(transactions);
        assertTrue(transactions.stream().allMatch(t -> t.getTransactionType().equals("outcome")));
    }

    @Test
    void testInvalidDateFormat() {
        assertThrows(Exception.class, () -> transactionServiceImpl.getTransactions(null, null, LocalDateTime.parse("invalid-date"), LocalDateTime.MAX, 0, 20));
    }

    @Test
    void testEmptyResult() {
        List<Transaction> transactions = transactionServiceImpl.getTransactions("9999999", null, LocalDateTime.MIN, LocalDateTime.MAX, 0, 20);
        assertNotNull(transactions);
        assertEquals(0, transactions.size());
    }

    @Test
    void testSingleTransactionPagination() {
        List<Transaction> transactions = transactionServiceImpl.getTransactions(null, null, LocalDateTime.MIN, LocalDateTime.MAX, 0, 1);
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }

    @Test
    void testBoundaryDates() {
        LocalDateTime start = LocalDateTime.of(2022, 12, 11, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 12, 11, 23, 59);
        List<Transaction> transactions = transactionServiceImpl.getTransactions(null, null, start, end, 0, 20);
        assertNotNull(transactions);
        assertTrue(transactions.stream().allMatch(t -> !t.getDateTime().isBefore(start) && !t.getDateTime().isAfter(end)));
    }

    @Test
    void testPageOutOfBounds() {
        List<Transaction> transactions = transactionServiceImpl.getTransactions(null, null, LocalDateTime.MIN, LocalDateTime.MAX, Integer.MAX_VALUE, 20);
        assertNotNull(transactions);
        assertEquals(0, transactions.size());
    }
}
