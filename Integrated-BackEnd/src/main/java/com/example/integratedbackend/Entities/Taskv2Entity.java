package com.example.integratedbackend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "taskv2", schema = "kradanitbangmod")
public class Taskv2Entity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer taskId;

    @Column(name = "taskTitle", nullable = false, length = 100)
    private String taskTitle;

    @Column(name = "taskDescription", nullable = true, length = 500)
    private String taskDescription;

    @Column(name = "taskAssignees", nullable = true, length = 30)
    private String taskAssignees;

    @Column(name = "taskStatusId", nullable = false, insertable = false, updatable = false)
    private Integer taskStatusId;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "taskStatusId", referencedColumnName = "statusId", nullable = false)
    private StatusEntity statusByTaskStatusId;
}

