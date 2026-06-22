package com.example.eventmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mesajul este obligatoriu")
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    private boolean read = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type = NotificationType.INFO;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    public enum NotificationType {
        INFO, WARNING, CONFIRMATION, CANCELLATION
    }
}