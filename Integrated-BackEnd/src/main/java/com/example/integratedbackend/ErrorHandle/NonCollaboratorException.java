package com.example.integratedbackend.ErrorHandle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NonCollaboratorException extends RuntimeException{
    public NonCollaboratorException(HttpStatus forbidden, String message)  {
        super(message);
    }
}
