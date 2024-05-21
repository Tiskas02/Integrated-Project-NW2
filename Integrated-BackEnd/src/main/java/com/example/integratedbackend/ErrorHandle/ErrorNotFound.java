package com.example.integratedbackend.ErrorHandle;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorNotFound {
    private final ZonedDateTime timestamp;
    private final int status;
    private final  String message;
    private final String instance;
    private List<ValidationError> errors;

    public ErrorNotFound(int status, String message, String instance) {
        this.status = status;
        this.message = message;
        this.instance = instance;
        this.timestamp = ZonedDateTime.now();
    }

    private record ValidationError(String field, String message) {
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }
}
