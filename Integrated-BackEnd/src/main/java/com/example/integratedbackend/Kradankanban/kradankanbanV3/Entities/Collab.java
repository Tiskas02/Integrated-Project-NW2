package com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "collab")
@IdClass(CollabId.class)
public class Collab {

    @Id
    @Column(name = "board_boardId", nullable = false)
    private String boardId;

    @Id
    @Column(name = "users_oid", nullable = false)
    private String userId;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "addedOn", insertable = false, updatable = false)
    private Date addedOn;

    @Column(name = "accessRight", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessRight accessRight = AccessRight.READ;

}
