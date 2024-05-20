package com.example.integratedbackend.ErrorHandle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemErrorNotFoundException extends RuntimeException{
    public ItemErrorNotFoundException(String message){
        super(message);
    }
}

