package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Receiver {
    @Column(name = "receiver_name")
    private String name;
    @Column(name = "receiver_address")
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_po_id")
    private PostalOffice postalOffice;
}
