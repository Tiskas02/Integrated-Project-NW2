package com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "status", schema = "kradankanbanV3")
public class StatusV3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusId", nullable = false)
    private Integer statusId;

    @Column(name = "statusName", nullable = false)
    private String statusName;

    @Column(name = "statusDescription")
    private String statusDescription;

    @ManyToOne
    @JoinColumn(name = "boardId", referencedColumnName = "boardId")
    private Boards board;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<TaskV3> tasks;
}
