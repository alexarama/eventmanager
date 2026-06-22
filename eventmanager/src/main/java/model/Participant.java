package com.example.eventmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele este obligatoriu")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Prenumele este obligatoriu")
    @Column(nullable = false)
    private String lastName;

    @Email(message = "Email invalid")
    @NotBlank(message = "Email-ul este obligatoriu")
    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<Registration> registrations;
}