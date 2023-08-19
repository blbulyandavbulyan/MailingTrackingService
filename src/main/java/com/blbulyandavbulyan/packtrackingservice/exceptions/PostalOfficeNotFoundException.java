package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class PostalOfficeNotFoundException extends ResourceNotFoundException {
    public PostalOfficeNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
