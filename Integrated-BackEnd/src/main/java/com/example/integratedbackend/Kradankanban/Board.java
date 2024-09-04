package com.example.integratedbackend.Kradankanban;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "boards", schema = "kradankanbanV3")
public class Board {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "boards_id", nullable = false)
    private String boardId;

    @Column(name = "boards_name", nullable = false)
    private String boardName;

}
