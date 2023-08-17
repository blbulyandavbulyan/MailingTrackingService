package com.blbulyandavbulyan.packtrackingservice.dtos;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;

public record MailingDTO(Long mailingId, Mailing.Type type, ReceiverDTO receiver) {
}
