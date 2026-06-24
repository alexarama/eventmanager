package com.example.eventmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import lombok.ToString;

@Data
@ToString(exclude = {"category", "location", "eventGroup", "participants", "registrations"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele evenimentului este obligatoriu")
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull(message = "Data de start este obligatorie")
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "Data de final este obligatorie")
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Integer availableSpots;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.OPEN;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "event_group_id")
    private EventGroup eventGroup;

    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Registration> registrations;

    public enum EventStatus {
        OPEN, CLOSED, CANCELLED
    }
}