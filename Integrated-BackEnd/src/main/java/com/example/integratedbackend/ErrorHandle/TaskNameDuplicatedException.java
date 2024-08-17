package com.example.integratedbackend.ErrorHandle;

public class TaskNameDuplicatedException extends RuntimeException {
    public TaskNameDuplicatedException(String message) {
        super(message);
    }
}
