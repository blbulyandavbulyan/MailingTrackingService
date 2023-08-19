package com.blbulyandavbulyan.packtrackingservice.exceptions;

public class MailingNotFoundException extends ResourceNotFoundException{
    public MailingNotFoundException(String message) {
        super(message);
    }
}
