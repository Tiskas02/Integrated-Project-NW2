package com.example.integratedbackend.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "status", schema = "kradanitbangmod")
public class Status {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "statusId", nullable = false)
    private Integer statusId;

    @Column(name = "statusName", nullable = false, length = 50)
    private String name;

    @Column(name = "statusDescription", nullable = true, length = 200)
    private String description;
}