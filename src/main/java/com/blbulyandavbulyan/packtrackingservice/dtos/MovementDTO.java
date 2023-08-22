package com.blbulyandavbulyan.packtrackingservice.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public final class MovementDTO {
    private Long movementId;
    private Long mailingId;
    private Instant arrivalDateTime;
    private Instant departureDateTime;
    public MovementDTO(Long movementId, Long mailingId, Instant arrivalDateTime, Instant departureDateTime) {
        this.movementId = movementId;
        this.mailingId = mailingId;
        this.arrivalDateTime = arrivalDateTime;
        this.departureDateTime = departureDateTime;
    }
}
