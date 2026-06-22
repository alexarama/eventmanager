package com.example.eventmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_groups")
public class EventGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele grupului este obligatoriu")
    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "eventGroup", cascade = CascadeType.ALL)
    private List<Event> events;
}