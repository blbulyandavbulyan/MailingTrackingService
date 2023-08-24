package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "mailing_movements")
@Getter
@Setter
public class MailingMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Long movementId;
    @ManyToOne
    @JoinColumn(name = "mailing_id")
    private Mailing mailing;
    @ManyToOne
    @JoinColumn(name = "postal_office_id")
    private PostalOffice postalOffice;
    @CreationTimestamp
    private Instant arrivalDateTime;
    private Instant departureDateTime;
}
