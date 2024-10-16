package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import lombok.Data;

import java.security.PrivateKey;
import java.util.Date;

@Data
public class CollabBoardDto {
    private String boardId;
    private String boardName;
    private String ownerBoard;
    private String userId;
    private Date addedOn;
    private AccessRight accessRight;

    public CollabBoardDto(String boardId, String boardName, String ownerBoard, String userId, Date addedOn, AccessRight accessRight) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.ownerBoard = ownerBoard;
        this.userId = userId;
        this.addedOn = addedOn;
        this.accessRight = accessRight;
    }
}

