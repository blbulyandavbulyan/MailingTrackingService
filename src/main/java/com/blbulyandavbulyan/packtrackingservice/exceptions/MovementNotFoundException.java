package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class MovementNotFoundException extends ResourceNotFoundException{
    public MovementNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
