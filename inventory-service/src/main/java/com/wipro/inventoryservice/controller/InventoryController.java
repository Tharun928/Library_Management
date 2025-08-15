package com.wipro.inventoryservice.controller;

import com.wipro.inventoryservice.entity.Inventory;
import com.wipro.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory API", description = "Manage inventory stock for books")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(
        summary = "Add stock for a book",
        description = "Increase the stock quantity for a specific book",
        responses = {
            @ApiResponse(responseCode = "200", description = "Stock added successfully",
                content = @Content(schema = @Schema(implementation = Inventory.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")
        }
    )
    @PostMapping
    public ResponseEntity<Inventory> addStock(
            @Parameter(description = "Book ID to add stock for", example = "1")
            @RequestParam Long bookId,
            @Parameter(description = "Quantity to add", example = "10")
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.addStock(bookId, quantity));
    }

    @Operation(
        summary = "Get all inventory stock",
        description = "Fetch the list of all book stocks in inventory",
        responses = {
            @ApiResponse(responseCode = "200", description = "Inventory list retrieved successfully",
                content = @Content(schema = @Schema(implementation = Inventory.class)))
        }
    )
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllStock() {
        return ResponseEntity.ok(inventoryService.getAllStock());
    }

    @Operation(
        summary = "Get stock by book ID",
        description = "Fetch the inventory stock for a specific book",
        responses = {
            @ApiResponse(responseCode = "200", description = "Stock retrieved successfully",
                content = @Content(schema = @Schema(implementation = Inventory.class))),
            @ApiResponse(responseCode = "404", description = "Book not found in inventory")
        }
    )
    @GetMapping("/{bookId}")
    public ResponseEntity<Inventory> getStockByBookId(
            @Parameter(description = "Book ID to fetch stock for", example = "1")
            @PathVariable Long bookId) {
        return ResponseEntity.ok(inventoryService.getStockByBookId(bookId));
    }

    @Operation(
        summary = "Update stock for a book",
        description = "Update the stock quantity for a specific book",
        responses = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully",
                content = @Content(schema = @Schema(implementation = Inventory.class))),
            @ApiResponse(responseCode = "404", description = "Book not found in inventory")
        }
    )
    @PutMapping("/{bookId}")
    public ResponseEntity<Inventory> updateStock(
            @Parameter(description = "Book ID to update stock for", example = "1")
            @PathVariable Long bookId,
            @Parameter(description = "New stock quantity", example = "15")
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.updateStock(bookId, quantity));
    }

    @Operation(
        summary = "Remove stock for a book",
        description = "Delete stock information for a specific book",
        responses = {
            @ApiResponse(responseCode = "204", description = "Stock removed successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found in inventory")
        }
    )
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeStock(
            @Parameter(description = "Book ID to remove stock for", example = "1")
            @PathVariable Long bookId) {
        inventoryService.removeStock(bookId);
        return ResponseEntity.noContent().build();
    }
}
