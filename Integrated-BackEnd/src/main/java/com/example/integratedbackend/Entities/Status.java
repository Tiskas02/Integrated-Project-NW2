package com.example.integratedbackend.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "status", schema = "kradanitbangmod")
@Getter
@Setter
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    private Integer statusId;
    @Column(name = "statusName")
    private String statusName;
    @Column(name = "statusDescription")
    private String statusDescription;


}
