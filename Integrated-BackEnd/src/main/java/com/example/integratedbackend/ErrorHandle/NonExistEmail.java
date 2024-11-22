package com.example.integratedbackend.ErrorHandle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NonExistEmail extends RuntimeException {
    public NonExistEmail(HttpStatus notFound, String message) {
        super(message);
    }
}
