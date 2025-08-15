package com.wipro.transactionservice.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {
	
    private static final long serialVersionUID = 1L; // Added serialVersionUID

    public TransactionNotFoundException(String message) {
        super(message);
    }
}