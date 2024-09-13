package com.example.integratedbackend.Kradankanban;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;


@Entity
@Data
@Table(name = "tasks", schema = "kradankanbanV3")
public class TaskV3 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tasks_id", nullable = false)
    private Integer id;

    @Column(name = "tasks_name", nullable = false)
    private String title;

    @Column(name = "tasks_description", nullable = true)
    private String description;

    @Column(name = "tasks_assignees", nullable = true)
    private String assignees;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "boards_boards_id", referencedColumnName = "boards_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "taskStatusId", referencedColumnName = "statuses_id")
    private StatusV3 status;
}
