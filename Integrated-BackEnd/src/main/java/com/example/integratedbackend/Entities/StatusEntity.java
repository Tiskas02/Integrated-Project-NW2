package com.example.integratedbackend.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "status", schema = "kradanitbangmod")
public class StatusEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "statusId", nullable = false)
    private Integer statusId;
    
    @Column(name = "statusName", nullable = false, length = 50)
    private String statusName;
    
    @Column(name = "statusDescription", nullable = true, length = 200)
    private String statusDescription;

    @OneToMany(mappedBy = "statusByTaskStatusId")
    private List<Taskv2Entity> taskv2sByStatusId;
}
