package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "mailings")
@Getter
@Setter
public class Mailing {
    public enum Type{LETTER, PACKAGE, WRAPPER, POSTCARD}
    @Id
    @Column(name = "mailing_id")
    @NotNull
    private Long packId;
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;
    @ManyToOne
    @JoinColumn(name = "destination_po_id")
    private PostalOffice destinationPostalOffice;
    @OneToMany(mappedBy = "mailing")
    private List<MailingMoving> mailingMovings;
}
