package com.example.eventmanager.repository;

import com.example.eventmanager.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByStatus(Event.EventStatus status, Pageable pageable);
    Page<Event> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Event> findByEventGroupId(Long eventGroupId, Pageable pageable);
    Optional<Event> findByJoinToken(String joinToken);
}