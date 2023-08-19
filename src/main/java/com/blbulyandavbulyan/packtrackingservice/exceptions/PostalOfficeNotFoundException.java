package com.blbulyandavbulyan.packtrackingservice.exceptions;

public class PostalOfficeNotFoundException extends ResourceNotFoundException{
    public PostalOfficeNotFoundException(String message) {
        super(message);
    }
}
