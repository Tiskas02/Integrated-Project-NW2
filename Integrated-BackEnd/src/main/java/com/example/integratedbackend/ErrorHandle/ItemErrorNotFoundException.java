package com.example.integratedbackend.ErrorHandle;

public class ItemErrorNotFoundException extends RuntimeException{
    public ItemErrorNotFoundException(String message){
        super(message);
    }
}

