package com.example.eventmanager.controller;

import com.example.eventmanager.model.Notification;
import com.example.eventmanager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/participant/{participantId}")
    public String findByParticipant(@PathVariable Long participantId, Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("notifications", notificationService.findByParticipantId(
                participantId, PageRequest.of(page, size, Sort.by("sentAt").descending())));
        model.addAttribute("participantId", participantId);
        return "notifications/list";
    }

    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id, @RequestParam Long participantId) {
        notificationService.markAsRead(id);
        return "redirect:/notifications/participant/" + participantId;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam Long participantId) {
        notificationService.delete(id);
        return "redirect:/notifications/participant/" + participantId;
    }
}