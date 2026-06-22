package com.example.eventmanager.repository;

import com.example.eventmanager.model.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Page<Registration> findByParticipantId(Long participantId, Pageable pageable);
    Page<Registration> findByEventId(Long eventId, Pageable pageable);
    boolean existsByEventIdAndParticipantId(Long eventId, Long participantId);
}