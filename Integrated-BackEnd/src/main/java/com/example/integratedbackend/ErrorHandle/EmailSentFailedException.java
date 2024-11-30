package com.example.integratedbackend.ErrorHandle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class EmailSentFailedException extends RuntimeException {

    public EmailSentFailedException(String message) {
        super(message);
    }
}
