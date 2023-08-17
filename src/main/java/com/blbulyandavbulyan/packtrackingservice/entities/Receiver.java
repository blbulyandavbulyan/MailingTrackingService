package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "receivers")
@Getter
@Setter
public class Receiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long receiverId;
    private String name;
    @ManyToOne
    @JoinTable(
            name = "receivers_addresses",
            joinColumns = @JoinColumn(name = "receiver_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Address address;
}
