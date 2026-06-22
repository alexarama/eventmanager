package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Location;
import com.example.eventmanager.repository.LocationRepository;
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
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setId(1L);
        location.setName("Test Location");
        location.setAddress("Test Address");
        location.setCity("Bucharest");
        location.setCountry("Romania");
        location.setCapacity(500);
    }

    @Test
    void findAll_returnsAllLocations() {
        when(locationRepository.findAll()).thenReturn(List.of(location));

        List<Location> result = locationService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void findById_existingId_returnsLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Location result = locationService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Location", result.getName());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(locationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> locationService.findById(99L));
    }

    @Test
    void save_validLocation_returnsSavedLocation() {
        when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.save(location);

        assertNotNull(result);
        assertEquals("Test Location", result.getName());
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void update_existingId_updatesLocation() {
        Location updated = new Location();
        updated.setName("Updated Location");
        updated.setAddress("Updated Address");
        updated.setCity("Cluj");
        updated.setCountry("Romania");
        updated.setCapacity(300);

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(updated);

        Location result = locationService.update(1L, updated);

        assertNotNull(result);
        assertEquals("Updated Location", result.getName());
    }

    @Test
    void delete_existingId_deletesLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        locationService.delete(1L);

        verify(locationRepository, times(1)).deleteById(1L);
    }
}