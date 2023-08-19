package com.blbulyandavbulyan.packtrackingservice.controllers.handling;

import com.blbulyandavbulyan.packtrackingservice.exceptions.ErrorMessage;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingTrackingServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(MailingTrackingServiceException.class)
    public ResponseEntity<ErrorMessage> handleMailingTrackingServiceException(MailingTrackingServiceException e){
        HttpStatus httpStatus = e.getHttpStatus();
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), Instant.now(), httpStatus.value()), httpStatus);
    }
}
