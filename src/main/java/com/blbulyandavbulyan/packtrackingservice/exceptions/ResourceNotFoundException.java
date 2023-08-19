package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends MailingTrackingServiceException{
    public ResourceNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
