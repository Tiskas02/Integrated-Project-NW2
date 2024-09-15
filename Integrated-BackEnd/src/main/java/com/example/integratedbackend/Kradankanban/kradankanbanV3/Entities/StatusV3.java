package com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "status", schema = "kradankanbanV3")
public class StatusV3 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "statusId", nullable = false)
    private int statusId;

    @Column(name = "statusName", nullable = false)
    private String statusName;

    @Column(name = "statusDescription")
    private String statusDescription;

    @ManyToOne
    @JoinColumn(name = "boardId", referencedColumnName = "boardId")
    private Boards boardId;
}
