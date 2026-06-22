package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.exception.BusinessException;
import com.example.eventmanager.model.Participant;
import com.example.eventmanager.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public Page<Participant> findAll(Pageable pageable) {
        return participantRepository.findAll(pageable);
    }

    public Participant findById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participantul cu id " + id + " nu a fost găsit"));
    }

    public Participant save(Participant participant) {
        if (participantRepository.findByEmail(participant.getEmail()).isPresent()) {
            throw new BusinessException("Email-ul " + participant.getEmail() + " este deja folosit");
        }
        return participantRepository.save(participant);
    }

    public Participant update(Long id, Participant participant) {
        Participant existing = findById(id);
        existing.setFirstName(participant.getFirstName());
        existing.setLastName(participant.getLastName());
        existing.setPhone(participant.getPhone());
        return participantRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        participantRepository.deleteById(id);
    }
}