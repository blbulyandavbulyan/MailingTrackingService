package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "postal_offices")
@Getter
@Setter
public class PostalOffice {
    @Id
    @Column(name = "postal_index", nullable = false)
    private Long index;
    @NotBlank
    private String title;
    @NotBlank
    private String address;
}
