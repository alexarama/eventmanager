package com.example.eventmanager.repository;

import com.example.eventmanager.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByParticipantId(Long participantId, Pageable pageable);
    Page<Notification> findByParticipantIdAndRead(Long participantId, boolean read, Pageable pageable);
}