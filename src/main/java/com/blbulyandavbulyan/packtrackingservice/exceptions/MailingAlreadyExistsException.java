package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class MailingAlreadyExistsException extends ResourceAlreadyExistsException{

    public MailingAlreadyExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
