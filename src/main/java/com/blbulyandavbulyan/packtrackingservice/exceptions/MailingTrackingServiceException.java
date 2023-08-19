package com.blbulyandavbulyan.packtrackingservice.exceptions;

import org.springframework.http.HttpStatus;

public class MailingTrackingServiceException extends RuntimeException{
    private final HttpStatus httpStatus;
    public MailingTrackingServiceException(String message,  HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public MailingTrackingServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
