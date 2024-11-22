package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import lombok.Data;

import java.util.Date;

@Data
public class CollabDTO {
    private String oid;
    private String boardId;
    private String ownerName;
    private String name;
    private String email;
    private AccessRight accessRight;
    private Date addedOn;
}
