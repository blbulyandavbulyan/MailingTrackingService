package com.blbulyandavbulyan.packtrackingservice.exceptions;

public class MailingTrackingServiceException extends RuntimeException{
    public MailingTrackingServiceException() {
    }

    public MailingTrackingServiceException(String message) {
        super(message);
    }

    public MailingTrackingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
