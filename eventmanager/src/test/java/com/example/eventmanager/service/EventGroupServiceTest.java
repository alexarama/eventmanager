package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.EventGroup;
import com.example.eventmanager.repository.EventGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventGroupServiceTest {

    @Mock
    private EventGroupRepository eventGroupRepository;

    @InjectMocks
    private EventGroupService eventGroupService;

    private EventGroup eventGroup;

    @BeforeEach
    void setUp() {
        eventGroup = new EventGroup();
        eventGroup.setId(1L);
        eventGroup.setName("Test Group");
        eventGroup.setDescription("Test Description");
    }

    @Test
    void findAll_returnsAllGroups() {
        when(eventGroupRepository.findAll()).thenReturn(List.of(eventGroup));
        List<EventGroup> result = eventGroupService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventGroupRepository, times(1)).findAll();
    }

    @Test
    void findById_existingId_returnsGroup() {
        when(eventGroupRepository.findById(1L)).thenReturn(Optional.of(eventGroup));
        EventGroup result = eventGroupService.findById(1L);
        assertNotNull(result);
        assertEquals("Test Group", result.getName());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(eventGroupRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> eventGroupService.findById(99L));
    }

    @Test
    void save_validGroup_returnsSavedGroup() {
        when(eventGroupRepository.save(eventGroup)).thenReturn(eventGroup);
        EventGroup result = eventGroupService.save(eventGroup);
        assertNotNull(result);
        assertEquals("Test Group", result.getName());
        verify(eventGroupRepository, times(1)).save(eventGroup);
    }

    @Test
    void update_existingId_updatesGroup() {
        EventGroup updated = new EventGroup();
        updated.setName("Updated Group");
        updated.setDescription("Updated Description");
        when(eventGroupRepository.findById(1L)).thenReturn(Optional.of(eventGroup));
        when(eventGroupRepository.save(any(EventGroup.class))).thenReturn(updated);
        EventGroup result = eventGroupService.update(1L, updated);
        assertNotNull(result);
        assertEquals("Updated Group", result.getName());
    }

    @Test
    void delete_existingId_deletesGroup() {
        when(eventGroupRepository.findById(1L)).thenReturn(Optional.of(eventGroup));
        eventGroupService.delete(1L);
        verify(eventGroupRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingId_throwsException() {
        when(eventGroupRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> eventGroupService.delete(99L));
    }
}