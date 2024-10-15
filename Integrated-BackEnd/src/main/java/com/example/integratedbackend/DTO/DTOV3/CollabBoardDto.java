package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import lombok.Data;

import java.util.Date;

@Data
public class CollabBoardDto {
    private String boardId;
    private String boardName;
    private String userId;
    private Date addedOn;
    private AccessRight accessRight;

    public CollabBoardDto(String boardId, String boardName, String userId, Date addedOn, AccessRight accessRight) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.userId = userId;
        this.addedOn = addedOn;
        this.accessRight = accessRight;
    }
}

