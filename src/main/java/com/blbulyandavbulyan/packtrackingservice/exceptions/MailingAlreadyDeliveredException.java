package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class MailingAlreadyDeliveredException extends MailingTrackingServiceException{
    public MailingAlreadyDeliveredException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
