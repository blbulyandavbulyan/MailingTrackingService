package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends MailingTrackingServiceException{
    public ResourceAlreadyExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
