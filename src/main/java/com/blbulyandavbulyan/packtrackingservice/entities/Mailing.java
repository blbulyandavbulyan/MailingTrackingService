package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mailings")
@Getter
@Setter
public class Mailing {
    public enum Type{LETTER, PACKAGE, WRAPPER, POSTCARD}
    @Id
    @Column(name = "pack_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packId;
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @ManyToOne
    @JoinColumn(name = "postal_office_id")
    public PostalOffice postalOffice;
}
