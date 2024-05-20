package com.example.integratedbackend.ErrorHandle;

public class StatusIdNotFoundException extends RuntimeException {
    public StatusIdNotFoundException(String message) {
        super(message);
    }
}
