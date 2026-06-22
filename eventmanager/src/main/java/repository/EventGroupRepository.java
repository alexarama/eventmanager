package com.example.eventmanager.repository;

import com.example.eventmanager.model.EventGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventGroupRepository extends JpaRepository<EventGroup, Long> {
}