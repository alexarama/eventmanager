package com.example.eventmanager.controller;

import com.example.eventmanager.client.UserServiceClient;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.repository.ParticipantRepository;
import com.example.eventmanager.repository.RegistrationRepository;
import com.example.eventmanager.repository.UserRepository;
import com.example.eventmanager.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final UserServiceClient userServiceClient;
    private final EventService eventService;

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

        // Redis Cache demonstrare
        List<Event> cachedEvents = eventService.findAllCached();
        model.addAttribute("cachedEventsCount", cachedEvents.size());
        log.info("Events from Redis cache: {}", cachedEvents.size());

        // Apelează user-service prin Feign
        try {
            List<Map<String, Object>> microserviceUsers = userServiceClient.findAll();
            model.addAttribute("microserviceUsers", microserviceUsers);
            model.addAttribute("microserviceConnected", true);
            log.info("Successfully fetched {} users from user-service", microserviceUsers.size());
        } catch (Exception e) {
            log.warn("Could not fetch users from user-service: {}", e.getMessage());
            model.addAttribute("microserviceUsers", Collections.emptyList());
            model.addAttribute("microserviceConnected", false);
        }

        return "dashboard";
    }
}