package com.example.integratedbackend.ErrorHandle;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class ErrorNotFound {
    private final Timestamp timestamp;
    private final int status;
    private final  String message;
    private final String instance;
}
