package com.blbulyandavbulyan.packtrackingservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
@Getter
@Setter
public class MovementCreatedDTO {
    private Long movementId;
    private Long mailingId;
    private Long postalOfficeId;
    private ZonedDateTime arrivalDateTime;
}
