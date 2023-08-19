package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class MailingNotFoundException extends ResourceNotFoundException{
    public MailingNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
