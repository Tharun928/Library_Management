package com.wipro.fineservice.controller;

import com.wipro.fineservice.entity.Fine;
import com.wipro.fineservice.service.FineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Tag(name = "Fine API", description = "Operations related to fines and payments")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @Operation(
        summary = "Create a fine",
        description = "Creates a new fine record for a user related to a specific book",
        responses = {
            @ApiResponse(responseCode = "200", description = "Fine created successfully",
                content = @Content(schema = @Schema(implementation = Fine.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
        }
    )
    @PostMapping
    public ResponseEntity<Fine> createFine(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam double amount) {
        return ResponseEntity.ok(fineService.createFine(userId, bookId, amount));
    }

    @Operation(
        summary = "Get fines by user",
        description = "Fetches all fines for a specific user by their ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of fines retrieved successfully",
                content = @Content(schema = @Schema(implementation = Fine.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    @GetMapping
    public ResponseEntity<List<Fine>> getFinesByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(fineService.getFinesByUser(userId));
    }

    @Operation(
        summary = "Mark fine as paid",
        description = "Marks a specific fine as paid",
        responses = {
            @ApiResponse(responseCode = "200", description = "Fine marked as paid successfully",
                content = @Content(schema = @Schema(implementation = Fine.class))),
            @ApiResponse(responseCode = "404", description = "Fine not found")
        }
    )
    @PutMapping("/{fineId}/pay")
    public ResponseEntity<Fine> markFineAsPaid(@PathVariable Long fineId) {
        return ResponseEntity.ok(fineService.markFineAsPaid(fineId));
    }

    @Operation(
        summary = "Get payment history",
        description = "Retrieves all payment transactions for all users",
        responses = {
            @ApiResponse(responseCode = "200", description = "Payment history retrieved successfully",
                content = @Content(schema = @Schema(implementation = Fine.class)))
        }
    )
    @GetMapping("/history")
    public ResponseEntity<List<Fine>> getPaymentHistory() {
        return ResponseEntity.ok(fineService.getAllPayments());
    }
}
