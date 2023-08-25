package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class MailingAlreadyExistsException extends ResourceAlreadyExistsException{

    public MailingAlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
