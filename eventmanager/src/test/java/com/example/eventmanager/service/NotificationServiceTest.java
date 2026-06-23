package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Notification;
import com.example.eventmanager.model.Participant;
import com.example.eventmanager.repository.NotificationRepository;
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
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private Participant participant;

    @BeforeEach
    void setUp() {
        participant = new Participant();
        participant.setId(1L);
        participant.setFirstName("Ion");
        participant.setLastName("Popescu");
        participant.setEmail("ion@test.com");

        notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test notification");
        notification.setType(Notification.NotificationType.INFO);
        notification.setRead(false);
        notification.setParticipant(participant);
    }

    @Test
    void findByParticipantId_returnsPage() {
        Page<Notification> page = new PageImpl<>(List.of(notification));
        when(notificationRepository.findByParticipantId(eq(1L), any(PageRequest.class))).thenReturn(page);
        Page<Notification> result = notificationService.findByParticipantId(1L, PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findById_existingId_returnsNotification() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        Notification result = notificationService.findById(1L);
        assertNotNull(result);
        assertEquals("Test notification", result.getMessage());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> notificationService.findById(99L));
    }

    @Test
    void save_validNotification_returnsSavedNotification() {
        when(participantService.findById(1L)).thenReturn(participant);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        Notification result = notificationService.save(1L, "Test notification", Notification.NotificationType.INFO);
        assertNotNull(result);
        assertEquals("Test notification", result.getMessage());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markAsRead_existingId_marksAsRead() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        Notification result = notificationService.markAsRead(1L);
        assertTrue(result.isRead());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void delete_existingId_deletesNotification() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        notificationService.delete(1L);
        verify(notificationRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingId_throwsException() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> notificationService.delete(99L));
    }
}