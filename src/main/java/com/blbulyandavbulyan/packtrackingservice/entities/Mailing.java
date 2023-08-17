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
    @SuppressWarnings("unused")
    public enum Type{LETTER, PACKAGE, WRAPPER, POSTCARD}
    @SuppressWarnings("unused")
    public enum Status{ON_THE_WAY, IN_THE_DESTINATION, DELIVERED}
    @Id
    @Column(name = "mailing_id")
    @NotNull
    private Long packId;
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "mailing")
    private List<MailingMovement> mailingMovements;
}
