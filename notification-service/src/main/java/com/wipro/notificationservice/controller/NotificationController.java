package com.wipro.notificationservice.controller;

import com.wipro.notificationservice.dto.NotificationRequest;
import com.wipro.notificationservice.entity.Notification;
import com.wipro.notificationservice.service.NotificationService;
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
@RequestMapping("/notifications")
@Tag(name = "Notification API", description = "APIs for creating and retrieving notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(
        summary = "Create a new notification",
        description = "Creates a notification with the given message",
        responses = {
            @ApiResponse(responseCode = "200", description = "Notification created successfully",
                content = @Content(schema = @Schema(implementation = Notification.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
        }
    )
    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Notification message payload",
                required = true,
                content = @Content(schema = @Schema(implementation = NotificationRequest.class))
            )
            @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.createNotification(request.getMessage()));
    }

    @Operation(
        summary = "Get all notifications",
        description = "Retrieves a list of all notifications in the system",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of notifications retrieved successfully",
                content = @Content(schema = @Schema(implementation = Notification.class)))
        }
    )
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @Operation(
        summary = "Get notification by ID",
        description = "Fetches a specific notification by its unique ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Notification retrieved successfully",
                content = @Content(schema = @Schema(implementation = Notification.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(
            @Parameter(description = "ID of the notification to retrieve", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }
}
