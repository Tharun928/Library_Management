package com.wipro.fineservice.service;




import com.wipro.fineservice.dto.NotificationRequest;
import com.wipro.fineservice.entity.Fine;
import com.wipro.fineservice.repository.FineRepository;
import com.wipro.fineservice.exception.FineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FineService {
    private final FineRepository fineRepository;
    private final RestTemplate restTemplate;
    private final String notificationServiceUrl = "http://localhost:8085/notifications";

    public FineService(FineRepository fineRepository, RestTemplate restTemplate) {
        this.fineRepository = fineRepository;
        this.restTemplate = restTemplate;
    }

    private void sendNotification(String message) {
        try {
            restTemplate.postForObject(notificationServiceUrl, new NotificationRequest(message), Void.class);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    public Fine createFine(Long userId, Long bookId, double amount) {
        Fine fine = new Fine();
        fine.setUserId(userId);
        fine.setBookId(bookId);
        fine.setAmount(amount);
        fine.setPaid(false);
        fine.setFineDate(LocalDateTime.now());
        Fine savedFine = fineRepository.save(fine);
        sendNotification("Fine created for user: " + userId + ", book: " + bookId);
        return savedFine;
    }

    public List<Fine> getFinesByUser(Long userId) {
        return fineRepository.findByUserId(userId);
    }

    public Fine markFineAsPaid(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new FineNotFoundException("Fine not found with id: " + fineId));
        fine.setPaid(true);
        fine.setPaymentDate(LocalDateTime.now());
        Fine updatedFine = fineRepository.save(fine);
        sendNotification("Fine paid: " + fineId);
        return updatedFine;
    }

    public List<Fine> getAllPayments() {
        return fineRepository.findAll();
    }
}