package com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CollabId implements Serializable {
    private String boardId;
    private String userId;
}
