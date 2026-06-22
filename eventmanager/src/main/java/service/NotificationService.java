package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Notification;
import com.example.eventmanager.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ParticipantService participantService;

    public Page<Notification> findByParticipantId(Long participantId, Pageable pageable) {
        return notificationRepository.findByParticipantId(participantId, pageable);
    }

    public Page<Notification> findUnreadByParticipantId(Long participantId, Pageable pageable) {
        return notificationRepository.findByParticipantIdAndRead(participantId, false, pageable);
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificarea cu id " + id + " nu a fost găsită"));
    }

    public Notification save(Long participantId, String message, Notification.NotificationType type) {
        Notification notification = new Notification();
        notification.setParticipant(participantService.findById(participantId));
        notification.setMessage(message);
        notification.setType(type);
        return notificationRepository.save(notification);
    }

    public Notification markAsRead(Long id) {
        Notification notification = findById(id);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void delete(Long id) {
        findById(id);
        notificationRepository.deleteById(id);
    }
}