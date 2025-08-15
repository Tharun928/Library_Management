package com.wipro.fineservice.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fines")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long bookId;
    private double amount;
    private boolean paid;
    private LocalDateTime fineDate;
    private LocalDateTime paymentDate;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public LocalDateTime getFineDate() { return fineDate; }
    public void setFineDate(LocalDateTime fineDate) { this.fineDate = fineDate; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
