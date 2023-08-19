package com.blbulyandavbulyan.packtrackingservice.exceptions;

import java.time.Instant;

public record ErrorMessage(String message, Instant timestamp, int statusCode) {
}
