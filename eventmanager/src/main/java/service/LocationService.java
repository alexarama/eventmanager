package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Location;
import com.example.eventmanager.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locația cu id " + id + " nu a fost găsită"));
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public Location update(Long id, Location location) {
        Location existing = findById(id);
        existing.setName(location.getName());
        existing.setAddress(location.getAddress());
        existing.setCity(location.getCity());
        existing.setCountry(location.getCountry());
        existing.setCapacity(location.getCapacity());
        return locationRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        locationRepository.deleteById(id);
    }
}