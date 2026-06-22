package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.exception.BusinessException;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.Registration;
import com.example.eventmanager.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventService eventService;
    private final ParticipantService participantService;

    public Page<Registration> findByParticipantId(Long participantId, Pageable pageable) {
        return registrationRepository.findByParticipantId(participantId, pageable);
    }

    public Page<Registration> findByEventId(Long eventId, Pageable pageable) {
        return registrationRepository.findByEventId(eventId, pageable);
    }

    public Registration findById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Înregistrarea cu id " + id + " nu a fost găsită"));
    }

    public Registration save(Long eventId, Long participantId) {
        Event event = eventService.findById(eventId);

        if (event.getStatus() == Event.EventStatus.CLOSED) {
            throw new BusinessException("Evenimentul este închis");
        }
        if (event.getAvailableSpots() <= 0) {
            throw new BusinessException("Nu mai sunt locuri disponibile");
        }
        if (registrationRepository.existsByEventIdAndParticipantId(eventId, participantId)) {
            throw new BusinessException("Participantul este deja înregistrat la acest eveniment");
        }

        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setParticipant(participantService.findById(participantId));

        event.setAvailableSpots(event.getAvailableSpots() - 1);
        eventService.save(event);

        return registrationRepository.save(registration);
    }

    public void delete(Long id) {
        Registration registration = findById(id);
        Event event = registration.getEvent();
        event.setAvailableSpots(event.getAvailableSpots() + 1);
        eventService.save(event);
        registrationRepository.deleteById(id);
    }
}