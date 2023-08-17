package com.blbulyandavbulyan.packtrackingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "addresses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"country", "city", "street", "houseNumber"})
)
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @Min(1)
    private Integer houseNumber;
}
