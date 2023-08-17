package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Receiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long receiverId;
    private String name;
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postal_office_id")
    private PostalOffice postalOffice;
}
