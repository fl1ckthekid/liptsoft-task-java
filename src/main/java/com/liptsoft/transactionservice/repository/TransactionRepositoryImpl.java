package com.liptsoft.transactionservice.repository;

import com.liptsoft.transactionservice.model.Transaction;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final String INCOMES_FILE = "incomes.csv";
    private static final String OUTCOMES_FILE = "outcomes.csv";

    @Override
    public List<Transaction> getTransactions(String accountId, String transactionType, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            if (transactionType == null || transactionType.equalsIgnoreCase("income")) {
                transactions.addAll(loadTransactionsFromFile(INCOMES_FILE, accountId, "income", startDate, endDate));
            }
            if (transactionType == null || transactionType.equalsIgnoreCase("outcome")) {
                transactions.addAll(loadTransactionsFromFile(OUTCOMES_FILE, accountId, "outcome", startDate, endDate));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while loading transactions: " + e.getMessage(), e);
        }

        return transactions.stream()
                .sorted((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    private List<Transaction> loadTransactionsFromFile(String fileName, String accountId, String transactionType, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("data/" + fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Transaction transaction = new Transaction();
                transaction.setTransactionId(fields[0]);
                transaction.setCustomerId(fields[1]);
                transaction.setAccountId(fields[2]);
                transaction.setTransactionType(transactionType);
                transaction.setAmount(Double.parseDouble(fields[3]));
                transaction.setDateTime(LocalDateTime.parse(fields[4]));

                if ((accountId == null || transaction.getAccountId().equals(accountId)) &&
                        (transaction.getDateTime().isAfter(startDate) || transaction.getDateTime().isEqual(startDate)) &&
                        (transaction.getDateTime().isBefore(endDate) || transaction.getDateTime().isEqual(endDate))) {
                    transactions.add(transaction);
                }
            }
        }
        return transactions;
    }
}
