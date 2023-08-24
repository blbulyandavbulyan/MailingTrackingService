package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Receiver {
    @NotBlank
    @Column(name = "receiver_name")
    private String name;
    @NotBlank
    @Column(name = "receiver_address")
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_po_id")
    private PostalOffice postalOffice;
}
