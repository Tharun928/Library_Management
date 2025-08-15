package com.wipro.transactionservice.service;





import com.wipro.transactionservice.dto.NotificationRequest;
import com.wipro.transactionservice.entity.Transaction;
import com.wipro.transactionservice.entity.TransactionType;
import com.wipro.transactionservice.repository.TransactionRepository;
import com.wipro.transactionservice.exception.TransactionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;
    private final String notificationServiceUrl = "http://localhost:8085/notifications";

    public TransactionService(TransactionRepository transactionRepository, RestTemplate restTemplate) {
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    private void sendNotification(String message) {
        try {
            restTemplate.postForObject(notificationServiceUrl, new NotificationRequest(message), Void.class);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    public Transaction borrowBook(Long userId, Long bookId) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setBookId(bookId);
        transaction.setTransactionType(TransactionType.BORROW);
        transaction.setTransactionDate(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        sendNotification("Book borrowed: " + bookId + " by user: " + userId);
        return savedTransaction;
    }

    public Transaction returnBook(Long userId, Long bookId) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setBookId(bookId);
        transaction.setTransactionType(TransactionType.RETURN);
        transaction.setTransactionDate(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        sendNotification("Book returned: " + bookId + " by user: " + userId);
        return savedTransaction;
    }

    public List<Transaction> getTransactions(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
    }
}
