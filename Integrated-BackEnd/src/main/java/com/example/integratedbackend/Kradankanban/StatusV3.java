package com.example.integratedbackend.Kradankanban;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "statuses", schema = "kradankanbanV3")
public class StatusV3 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "statuses_id", nullable = false)
    private Integer id;

    @Column(name = "statuses_name", nullable = false, length = 50)
    private String name;

    @Column(name = "statuses_description", nullable = true, length = 200)
    private String description;
}

