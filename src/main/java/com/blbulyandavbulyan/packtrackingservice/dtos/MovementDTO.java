package com.blbulyandavbulyan.packtrackingservice.dtos;

import java.time.ZonedDateTime;

public record MovementDTO(Long movementId, Long receiverId, Long mailingId, ZonedDateTime arrivalDateTime, ZonedDateTime departureDateTime) {
}
