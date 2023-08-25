package com.blbulyandavbulyan.packtrackingservice.dtos;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MailingDTO(@NotNull @Min(1) Long id, @NotNull Mailing.Type type, @Valid @NotNull ReceiverDTO receiver) {
}
