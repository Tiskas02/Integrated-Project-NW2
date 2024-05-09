package com.example.integratedbackend.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "taskv2", schema = "kradanitbangmod")
@Getter
@Setter
public class TasksV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    private Integer taskId;

    @Column(name = "taskTitle")
    private String taskTitle;

    @Column(name = "taskDescription")
    private String taskDescription;

    @Column(name = "taskAssignees")
    private String taskAssignees;

    @ManyToOne
    @JoinColumn(name = "taskStatus", referencedColumnName = "statusId")
    private Status status;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

}
