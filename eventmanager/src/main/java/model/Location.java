package com.example.eventmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele locației este obligatoriu")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Adresa este obligatorie")
    @Column(nullable = false)
    private String address;

    private String city;

    private String country;

    private Integer capacity;
}