package com.example.eventmanager.service;

import com.example.eventmanager.exception.BusinessException;
import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setStartDate(LocalDateTime.now().plusDays(1));
        event.setEndDate(LocalDateTime.now().plusDays(2));
        event.setAvailableSpots(100);
        event.setStatus(Event.EventStatus.OPEN);
    }

    @Test
    void findAll_returnsPage() {
        Page<Event> page = new PageImpl<>(List.of(event));
        when(eventRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Event> result = eventService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(eventRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void findById_existingId_returnsEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Event", result.getName());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.findById(99L));
    }

    @Test
    void save_validEvent_returnsSavedEvent() {
        when(eventRepository.save(event)).thenReturn(event);

        Event result = eventService.save(event);

        assertNotNull(result);
        assertEquals("Test Event", result.getName());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void save_invalidDates_throwsBusinessException() {
        event.setEndDate(LocalDateTime.now().minusDays(1));

        assertThrows(BusinessException.class, () -> eventService.save(event));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void delete_existingId_deletesEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.delete(1L);

        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingId_throwsException() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.delete(99L));
        verify(eventRepository, never()).deleteById(any());
    }
}