package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class MovementAlreadyClosedException extends MailingTrackingServiceException{
    public MovementAlreadyClosedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
