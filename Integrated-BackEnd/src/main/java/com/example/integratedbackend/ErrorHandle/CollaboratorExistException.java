package com.example.integratedbackend.ErrorHandle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CollaboratorExistException extends RuntimeException {
    public CollaboratorExistException(HttpStatus status, String message) { super(message);}
}
