package com.blbulyandavbulyan.packtrackingservice.dtos;

import java.time.Instant;
import java.time.ZonedDateTime;

public record MovementDTO(Long movementId, Long mailingId, Instant arrivalDateTime, Instant departureDateTime) {
}
