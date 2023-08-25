package com.blbulyandavbulyan.packtrackingservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceiverDTO(@NotNull @Min(1) Long index, @NotBlank String name, @NotBlank String address) {
}
