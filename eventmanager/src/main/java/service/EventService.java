package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.exception.BusinessException;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Page<Event> findAll(Pageable pageable) {
        log.debug("Fetching all events, page: {}", pageable.getPageNumber());
        return eventRepository.findAll(pageable);
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

    public Event save(Event event) {
        if (event.getEndDate().isBefore(event.getStartDate())) {
            log.error("Invalid event dates: startDate={}, endDate={}", event.getStartDate(), event.getEndDate());
            throw new BusinessException("Data de final nu poate fi înainte de data de start");
        }
        if (event.getJoinToken() == null || event.getJoinToken().isEmpty()) {
            event.setJoinToken(UUID.randomUUID().toString());
        }
        log.info("Saving event: {}", event.getName());
        return eventRepository.save(event);
    }

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

    public void delete(Long id) {
        log.info("Deleting event with id: {}", id);
        findById(id);
        eventRepository.deleteById(id);
    }
}