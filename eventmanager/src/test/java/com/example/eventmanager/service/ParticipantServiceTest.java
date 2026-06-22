package com.example.eventmanager.service;

import com.example.eventmanager.exception.BusinessException;
import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Participant;
import com.example.eventmanager.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    private Participant participant;

    @BeforeEach
    void setUp() {
        participant = new Participant();
        participant.setId(1L);
        participant.setFirstName("Ion");
        participant.setLastName("Popescu");
        participant.setEmail("ion.popescu@example.com");
        participant.setPhone("0721000000");
    }

    @Test
    void findAll_returnsPage() {
        Page<Participant> page = new PageImpl<>(List.of(participant));
        when(participantRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Participant> result = participantService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findById_existingId_returnsParticipant() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        Participant result = participantService.findById(1L);

        assertNotNull(result);
        assertEquals("Ion", result.getFirstName());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(participantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> participantService.findById(99L));
    }

    @Test
    void save_newEmail_savesParticipant() {
        when(participantRepository.findByEmail(participant.getEmail())).thenReturn(Optional.empty());
        when(participantRepository.save(participant)).thenReturn(participant);

        Participant result = participantService.save(participant);

        assertNotNull(result);
        assertEquals("ion.popescu@example.com", result.getEmail());
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    void save_duplicateEmail_throwsBusinessException() {
        when(participantRepository.findByEmail(participant.getEmail())).thenReturn(Optional.of(participant));

        assertThrows(BusinessException.class, () -> participantService.save(participant));
        verify(participantRepository, never()).save(any());
    }

    @Test
    void delete_existingId_deletesParticipant() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        participantService.delete(1L);

        verify(participantRepository, times(1)).deleteById(1L);
    }
}