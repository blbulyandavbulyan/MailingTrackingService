package com.blbulyandavbulyan.packtrackingservice.dtos;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;

import java.util.List;

public record MailingInfoDTO(Long mailingId, Mailing.Type type, Mailing.Status status, List<MovementDTO> movements) {
}
