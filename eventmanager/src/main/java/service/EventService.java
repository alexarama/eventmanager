package com.example.eventmanager.service;

import com.example.eventmanager.client.NotificationServiceClient;
import com.example.eventmanager.client.UserServiceClient;
import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.exception.BusinessException;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final NotificationServiceClient notificationServiceClient;
    private final UserServiceClient userServiceClient;

    public Page<Event> findAll(Pageable pageable) {
        log.debug("Fetching all events, page: {}", pageable.getPageNumber());
        return eventRepository.findAll(pageable);
    }

    @Cacheable(value = "events")
    public List<Event> findAllCached() {
        log.debug("Fetching all events from cache");
        return eventRepository.findAll();
    }

    public Page<Event> findByStatus(Event.EventStatus status, Pageable pageable) {
        return eventRepository.findByStatus(status, pageable);
    }

    public Page<Event> findByCategoryId(Long categoryId, Pageable pageable) {
        return eventRepository.findByCategoryId(categoryId, pageable);
    }

    public Event findById(Long id) {
        log.debug("Fetching event with id: {}", id);
        return eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Event not found with id: {}", id);
                    return new ResourceNotFoundException("Evenimentul cu id " + id + " nu a fost găsit");
                });
    }

    public Optional<Event> findByJoinToken(String token) {
        return eventRepository.findByJoinToken(token);
    }

    public Page<Event> search(String query, Pageable pageable) {
        log.debug("Searching events with query: {}", query);
        return eventRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable);
    }

    @CacheEvict(value = "events", allEntries = true)
    public Event save(Event event) {
        if (event.getEndDate().isBefore(event.getStartDate())) {
            log.error("Invalid event dates: startDate={}, endDate={}", event.getStartDate(), event.getEndDate());
            throw new BusinessException("Data de final nu poate fi înainte de data de start");
        }
        if (event.getJoinToken() == null || event.getJoinToken().isEmpty()) {
            event.setJoinToken(UUID.randomUUID().toString());
        }
        Event saved = eventRepository.save(event);
        log.info("Saving event: {}", event.getName());

        // Trimite notificare la notification-service
        try {
            // Obtine token JWT de la user-service
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", "admin");
            credentials.put("password", "admin123");
            Map<String, String> authResponse = userServiceClient.authenticate(credentials);
            String jwtToken = "Bearer " + authResponse.get("token");

            Map<String, Object> notification = new HashMap<>();
            notification.put("message", "Eveniment nou creat: " + saved.getName());
            notification.put("eventId", saved.getId());
            notification.put("type", "EVENT_CREATED");
            notificationServiceClient.createNotification(1L, "Eveniment nou creat: " + saved.getName(), "INFO", jwtToken);
            log.info("Notification sent to notification-service for event: {}", saved.getName());
        } catch (Exception e) {
            log.warn("Could not send notification to notification-service: {}", e.getMessage());
        }

        return saved;
    }

    @CacheEvict(value = "events", allEntries = true)
    public Event update(Long id, Event event) {
        Event existing = findById(id);
        existing.setName(event.getName());
        existing.setDescription(event.getDescription());
        existing.setStartDate(event.getStartDate());
        existing.setEndDate(event.getEndDate());
        existing.setAvailableSpots(event.getAvailableSpots());
        existing.setStatus(event.getStatus());
        existing.setLocation(event.getLocation());
        existing.setCategory(event.getCategory());
        existing.setEventGroup(event.getEventGroup());
        log.info("Updating event with id: {}", id);
        return eventRepository.save(existing);
    }

    @CacheEvict(value = "events", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting event with id: {}", id);
        findById(id);
        eventRepository.deleteById(id);
    }
}