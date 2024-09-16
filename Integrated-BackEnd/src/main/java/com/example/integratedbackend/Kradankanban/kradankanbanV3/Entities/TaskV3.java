package com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "task", schema = "kradankanbanV3")
public class TaskV3 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "assignees")
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
    @JoinColumn(name = "statusId", referencedColumnName = "statusId")
    private StatusV3 status;

    @ManyToOne
    @JoinColumn(name = "boardId", referencedColumnName = "boardId")
    private Boards board;
}
