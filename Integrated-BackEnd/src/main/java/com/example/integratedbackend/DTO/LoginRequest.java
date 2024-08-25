package com.example.integratedbackend.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

//    @NotEmpty(message = "Username cannot be empty")
//    @Size(max = 50, message = "Username cannot be longer than 50 characters")
    private String username;

//    @NotEmpty(message = "Password cannot be empty")
//    @Size(max = 14, message = "Password cannot be longer than 14 characters")
    private String password;
}
