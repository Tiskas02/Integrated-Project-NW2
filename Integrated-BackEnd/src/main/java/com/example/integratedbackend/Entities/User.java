package com.example.integratedbackend.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Username cannot be empty")
    @Size(max = 50, message = "Username cannot be longer than 50 characters")
    private String username;

    @Column(nullable = false)
    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 14, message = "Password cannot be longer than 14 characters")
    private String passwordHash;
}
