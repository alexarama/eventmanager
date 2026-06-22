package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.EventGroup;
import com.example.eventmanager.repository.EventGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventGroupService {

    private final EventGroupRepository eventGroupRepository;

    public List<EventGroup> findAll() {
        return eventGroupRepository.findAll();
    }

    public EventGroup findById(Long id) {
        return eventGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupul cu id " + id + " nu a fost găsit"));
    }

    public EventGroup save(EventGroup eventGroup) {
        return eventGroupRepository.save(eventGroup);
    }

    public EventGroup update(Long id, EventGroup eventGroup) {
        EventGroup existing = findById(id);
        existing.setName(eventGroup.getName());
        existing.setDescription(eventGroup.getDescription());
        return eventGroupRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        eventGroupRepository.deleteById(id);
    }
}