package com.example.eventmanager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

    @PostMapping("/api/notifications/participant/{participantId}")
    Map<String, Object> createNotification(
            @PathVariable Long participantId,
            @RequestParam String message,
            @RequestParam String type,
            @RequestHeader("Authorization") String token
    );
}