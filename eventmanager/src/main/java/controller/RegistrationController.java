package com.example.eventmanager.controller;

import com.example.eventmanager.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/event/{eventId}")
    public String findByEvent(@PathVariable Long eventId, Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("registrations", registrationService.findByEventId(eventId, PageRequest.of(page, size, Sort.by("registrationDate").descending())));
        model.addAttribute("eventId", eventId);
        return "registrations/list";
    }

    @GetMapping("/participant/{participantId}")
    public String findByParticipant(@PathVariable Long participantId, Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("registrations", registrationService.findByParticipantId(participantId, PageRequest.of(page, size, Sort.by("registrationDate").descending())));
        model.addAttribute("participantId", participantId);
        return "registrations/list";
    }

    @PostMapping("/event/{eventId}/participant/{participantId}")
    public String create(@PathVariable Long eventId, @PathVariable Long participantId) {
        registrationService.save(eventId, participantId);
        return "redirect:/events/" + eventId;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        registrationService.delete(id);
        return "redirect:/events";
    }
}