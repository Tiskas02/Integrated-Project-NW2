package com.example.integratedbackend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Size( max = 50, message = "size must be between 0 and 50")
    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    private String userName;

    @NotNull(message = "must not be null")
    @Size(max = 14, message = "size must be between 0 and 14")
    @NotBlank(message = "must not be blank")
    private String password;
}
