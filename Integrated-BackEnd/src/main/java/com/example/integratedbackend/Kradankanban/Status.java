package com.example.integratedbackend.Kradankanban;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "status", schema = "kradanitbangmod")
public class Status {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "statusId", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = true, length = 200)
    private String description;
}