package com.example.eventmanager.controller;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.repository.ParticipantRepository;
import com.example.eventmanager.repository.RegistrationRepository;
import com.example.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalEvents", eventRepository.count());
        model.addAttribute("totalParticipants", participantRepository.count());
        model.addAttribute("totalRegistrations", registrationRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("openEvents", eventRepository.countByStatus(Event.EventStatus.OPEN));
        model.addAttribute("closedEvents", eventRepository.countByStatus(Event.EventStatus.CLOSED));
        model.addAttribute("cancelledEvents", eventRepository.countByStatus(Event.EventStatus.CANCELLED));
        model.addAttribute("recentEvents", eventRepository.findAll(
                PageRequest.of(0, 5, Sort.by("startDate").descending())).getContent());
        return "dashboard";
    }
}