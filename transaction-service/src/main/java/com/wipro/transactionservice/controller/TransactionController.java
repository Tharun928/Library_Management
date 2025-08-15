package com.wipro.transactionservice.controller;

import com.wipro.transactionservice.entity.Transaction;
import com.wipro.transactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transaction API", description = "APIs for borrowing and returning books, and viewing transaction history")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
        summary = "Borrow a book",
        description = "Records a borrow transaction for a specific user and book",
        responses = {
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully",
                content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "User or book not found")
        }
    )
    @PostMapping("/borrow")
    public ResponseEntity<Transaction> borrowBook(
            @Parameter(description = "User ID borrowing the book", example = "1") @RequestParam Long userId,
            @Parameter(description = "Book ID being borrowed", example = "101") @RequestParam Long bookId) {
        return ResponseEntity.ok(transactionService.borrowBook(userId, bookId));
    }

    @Operation(
        summary = "Return a book",
        description = "Records a return transaction for a specific user and book",
        responses = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully",
                content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
        }
    )
    @PostMapping("/return")
    public ResponseEntity<Transaction> returnBook(
            @Parameter(description = "User ID returning the book", example = "1") @RequestParam Long userId,
            @Parameter(description = "Book ID being returned", example = "101") @RequestParam Long bookId) {
        return ResponseEntity.ok(transactionService.returnBook(userId, bookId));
    }

    @Operation(
        summary = "Get transactions for a user",
        description = "Fetches all transactions for a specific user, optionally filtered by date range",
        responses = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                content = @Content(schema = @Schema(implementation = Transaction.class)))
        }
    )
    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            @Parameter(description = "User ID to fetch transactions for", example = "1") @RequestParam Long userId,
            @Parameter(description = "Start date (optional, ISO format)", example = "2025-08-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (optional, ISO format)", example = "2025-08-14T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.getTransactions(userId, startDate, endDate));
    }

    @Operation(
        summary = "Get a transaction by ID",
        description = "Fetches a specific transaction using its unique ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully",
                content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(
            @Parameter(description = "Transaction ID", example = "5001") @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
