package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "mailing_movements")
@Getter
@Setter
public class MailingMoving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_unit_id")
    private Long routeUnitId;
    @ManyToOne
    @JoinColumn(name = "mailing_id")
    private Mailing mailing;
    @ManyToOne
    @JoinColumn(name = "postal_office_id")
    private PostalOffice postalOffice;
    @CreationTimestamp
    private ZonedDateTime arrivalDateTime;
    private ZonedDateTime departureDateTime;
}
