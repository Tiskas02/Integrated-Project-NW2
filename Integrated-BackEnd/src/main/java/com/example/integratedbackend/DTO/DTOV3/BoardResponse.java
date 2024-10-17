package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class BoardResponse {
    private String id;
    private String name;
    private String visibility;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
    private Users users;
    private CollabBoardResponse collaborator;

}
