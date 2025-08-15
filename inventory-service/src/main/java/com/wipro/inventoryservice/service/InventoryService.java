package com.wipro.inventoryservice.service;



import com.wipro.inventoryservice.dto.NotificationRequest;
import com.wipro.inventoryservice.entity.Inventory;
import com.wipro.inventoryservice.repository.InventoryRepository;
import com.wipro.inventoryservice.exception.InventoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate;
    private final String notificationServiceUrl = "http://localhost:8085/notifications";

    public InventoryService(InventoryRepository inventoryRepository, RestTemplate restTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.restTemplate = restTemplate;
    }

    private void sendNotification(String message) {
        try {
            restTemplate.postForObject(notificationServiceUrl, new NotificationRequest(message), Void.class);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    public Inventory addStock(Long bookId, int quantity) {
        Inventory inventory = new Inventory();
        inventory.setBookId(bookId);
        inventory.setQuantity(quantity);
        Inventory savedInventory = inventoryRepository.save(inventory);
        sendNotification("Stock added for book: " + bookId);
        return savedInventory;
    }

    public List<Inventory> getAllStock() {
        return inventoryRepository.findAll();
    }

    public Inventory getStockByBookId(Long bookId) {
        return inventoryRepository.findByBookId(bookId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for book id: " + bookId));
    }

    public Inventory updateStock(Long bookId, int quantity) {
        Inventory inventory = getStockByBookId(bookId);
        inventory.setQuantity(quantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        sendNotification("Stock updated for book: " + bookId);
        return updatedInventory;
    }

    public void removeStock(Long bookId) {
        Inventory inventory = getStockByBookId(bookId);
        inventoryRepository.delete(inventory);
        sendNotification("Stock removed for book: " + bookId);
    }
}
