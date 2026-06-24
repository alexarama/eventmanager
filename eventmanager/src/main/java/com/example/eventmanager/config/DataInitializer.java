package com.example.eventmanager.config;

import com.example.eventmanager.model.Category;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.EventGroup;
import com.example.eventmanager.model.Location;
import com.example.eventmanager.model.Participant;
import com.example.eventmanager.model.User;
import com.example.eventmanager.repository.CategoryRepository;
import com.example.eventmanager.repository.EventGroupRepository;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.repository.LocationRepository;
import com.example.eventmanager.repository.ParticipantRepository;
import com.example.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventGroupRepository eventGroupRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(User.Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole(User.Role.USER);
                user.setEnabled(true);
                userRepository.save(user);
            }

            if (categoryRepository.count() == 0) {
                Category tech = new Category();
                tech.setName("Tehnologie");
                tech.setDescription("Evenimente tech, conferinte IT, hackathoane");
                categoryRepository.save(tech);

                Category business = new Category();
                business.setName("Business");
                business.setDescription("Conferinte de afaceri, networking");
                categoryRepository.save(business);

                Category culture = new Category();
                culture.setName("Cultura");
                culture.setDescription("Evenimente culturale, expozitii, concerte");
                categoryRepository.save(culture);
            }

            if (locationRepository.count() == 0) {
                Location loc1 = new Location();
                loc1.setName("Sala Polivalenta");
                loc1.setAddress("Bd. Basarabia 37-39");
                loc1.setCity("Bucuresti");
                loc1.setCountry("Romania");
                loc1.setCapacity(5000);
                locationRepository.save(loc1);

                Location loc2 = new Location();
                loc2.setName("Palatul Parlamentului");
                loc2.setAddress("Str. Izvor 2-4");
                loc2.setCity("Bucuresti");
                loc2.setCountry("Romania");
                loc2.setCapacity(1000);
                locationRepository.save(loc2);

                Location loc3 = new Location();
                loc3.setName("Biblioteca Nationala");
                loc3.setAddress("Bd. Unirii 22");
                loc3.setCity("Bucuresti");
                loc3.setCountry("Romania");
                loc3.setCapacity(500);
                locationRepository.save(loc3);
            }

            if (eventGroupRepository.count() == 0) {
                EventGroup group1 = new EventGroup();
                group1.setName("Tech Summit 2026");
                group1.setDescription("Seria de evenimente tech ale anului 2026");
                eventGroupRepository.save(group1);

                EventGroup group2 = new EventGroup();
                group2.setName("Business Forum");
                group2.setDescription("Forum de afaceri si networking");
                eventGroupRepository.save(group2);
            }

            if (eventRepository.count() == 0) {
                Category tech = categoryRepository.findAll().get(0);
                Category business = categoryRepository.findAll().get(1);
                Location loc1 = locationRepository.findAll().get(0);
                Location loc2 = locationRepository.findAll().get(1);
                Location loc3 = locationRepository.findAll().get(2);
                EventGroup group1 = eventGroupRepository.findAll().get(0);
                EventGroup group2 = eventGroupRepository.findAll().get(1);

                Event e1 = new Event();
                e1.setName("Java Conference 2026");
                e1.setDescription("Cea mai mare conferinta Java din Romania");
                e1.setStartDate(LocalDateTime.of(2026, 7, 15, 9, 0));
                e1.setEndDate(LocalDateTime.of(2026, 7, 15, 18, 0));
                e1.setAvailableSpots(200);
                e1.setStatus(Event.EventStatus.OPEN);
                e1.setCategory(tech);
                e1.setLocation(loc1);
                e1.setEventGroup(group1);
                eventRepository.save(e1);

                Event e2 = new Event();
                e2.setName("Spring Boot Workshop");
                e2.setDescription("Workshop practic de Spring Boot pentru incepatori");
                e2.setStartDate(LocalDateTime.of(2026, 8, 1, 10, 0));
                e2.setEndDate(LocalDateTime.of(2026, 8, 1, 16, 0));
                e2.setAvailableSpots(50);
                e2.setStatus(Event.EventStatus.OPEN);
                e2.setCategory(tech);
                e2.setLocation(loc3);
                e2.setEventGroup(group1);
                eventRepository.save(e2);

                Event e3 = new Event();
                e3.setName("Startup Summit");
                e3.setDescription("Summit pentru antreprenori si investitori");
                e3.setStartDate(LocalDateTime.of(2026, 9, 10, 9, 0));
                e3.setEndDate(LocalDateTime.of(2026, 9, 10, 17, 0));
                e3.setAvailableSpots(300);
                e3.setStatus(Event.EventStatus.OPEN);
                e3.setCategory(business);
                e3.setLocation(loc2);
                e3.setEventGroup(group2);
                eventRepository.save(e3);

                Event e4 = new Event();
                e4.setName("AI & Machine Learning Day");
                e4.setDescription("Ziua dedicata inteligentei artificiale");
                e4.setStartDate(LocalDateTime.of(2026, 10, 5, 9, 0));
                e4.setEndDate(LocalDateTime.of(2026, 10, 5, 18, 0));
                e4.setAvailableSpots(150);
                e4.setStatus(Event.EventStatus.OPEN);
                e4.setCategory(tech);
                e4.setLocation(loc1);
                e4.setEventGroup(group1);
                eventRepository.save(e4);

                Event e5 = new Event();
                e5.setName("Networking Evening");
                e5.setDescription("Seara de networking pentru profesionisti");
                e5.setStartDate(LocalDateTime.of(2026, 7, 20, 18, 0));
                e5.setEndDate(LocalDateTime.of(2026, 7, 20, 21, 0));
                e5.setAvailableSpots(100);
                e5.setStatus(Event.EventStatus.OPEN);
                e5.setCategory(business);
                e5.setLocation(loc3);
                e5.setEventGroup(group2);
                eventRepository.save(e5);
            }

            if (participantRepository.count() == 0) {
                Participant p1 = new Participant();
                p1.setFirstName("Ion");
                p1.setLastName("Popescu");
                p1.setEmail("ion.popescu@example.com");
                p1.setPhone("0721000001");
                participantRepository.save(p1);

                Participant p2 = new Participant();
                p2.setFirstName("Maria");
                p2.setLastName("Ionescu");
                p2.setEmail("maria.ionescu@example.com");
                p2.setPhone("0721000002");
                participantRepository.save(p2);

                Participant p3 = new Participant();
                p3.setFirstName("Alexandru");
                p3.setLastName("Constantin");
                p3.setEmail("alex.constantin@example.com");
                p3.setPhone("0721000003");
                participantRepository.save(p3);
            }
        };
    }
}