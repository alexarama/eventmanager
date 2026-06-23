package com.example.eventmanager.service;

import com.example.eventmanager.exception.BusinessException;
import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.Participant;
import com.example.eventmanager.model.Registration;
import com.example.eventmanager.repository.RegistrationRepository;
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
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private EventService eventService;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private RegistrationService registrationService;

    private Event event;
    private Participant participant;
    private Registration registration;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setStartDate(LocalDateTime.now().plusDays(1));
        event.setEndDate(LocalDateTime.now().plusDays(2));
        event.setAvailableSpots(10);
        event.setStatus(Event.EventStatus.OPEN);

        participant = new Participant();
        participant.setId(1L);
        participant.setFirstName("Ion");
        participant.setLastName("Popescu");
        participant.setEmail("ion@test.com");

        registration = new Registration();
        registration.setId(1L);
        registration.setEvent(event);
        registration.setParticipant(participant);
        registration.setStatus(Registration.RegistrationStatus.PENDING);
    }

    @Test
    void findByEventId_returnsPage() {
        Page<Registration> page = new PageImpl<>(List.of(registration));
        when(registrationRepository.findByEventId(eq(1L), any(PageRequest.class))).thenReturn(page);
        Page<Registration> result = registrationService.findByEventId(1L, PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findById_existingId_returnsRegistration() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        Registration result = registrationService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(registrationRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> registrationService.findById(99L));
    }

    @Test
    void save_validRegistration_savesAndDecreasesSpots() {
        when(eventService.findById(1L)).thenReturn(event);
        when(participantService.findById(1L)).thenReturn(participant);
        when(registrationRepository.existsByEventIdAndParticipantId(1L, 1L)).thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);
        when(eventService.save(any(Event.class))).thenReturn(event);

        Registration result = registrationService.save(1L, 1L);

        assertNotNull(result);
        assertEquals(9, event.getAvailableSpots());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void save_closedEvent_throwsBusinessException() {
        event.setStatus(Event.EventStatus.CLOSED);
        when(eventService.findById(1L)).thenReturn(event);
        assertThrows(BusinessException.class, () -> registrationService.save(1L, 1L));
    }

    @Test
    void save_noSpotsAvailable_throwsBusinessException() {
        event.setAvailableSpots(0);
        when(eventService.findById(1L)).thenReturn(event);
        assertThrows(BusinessException.class, () -> registrationService.save(1L, 1L));
    }

    @Test
    void save_duplicateRegistration_throwsBusinessException() {
        when(eventService.findById(1L)).thenReturn(event);
        when(registrationRepository.existsByEventIdAndParticipantId(1L, 1L)).thenReturn(true);
        assertThrows(BusinessException.class, () -> registrationService.save(1L, 1L));
    }

    @Test
    void delete_existingId_deletesAndIncreasesSpots() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(eventService.save(any(Event.class))).thenReturn(event);

        registrationService.delete(1L);

        assertEquals(11, event.getAvailableSpots());
        verify(registrationRepository, times(1)).deleteById(1L);
    }
}