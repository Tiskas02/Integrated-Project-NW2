package com.example.integratedbackend.ErrorHandle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
public class ItemErrorNotFoundException extends RuntimeException{
    public ItemErrorNotFoundException(String message){
        super(message);
    }
}

