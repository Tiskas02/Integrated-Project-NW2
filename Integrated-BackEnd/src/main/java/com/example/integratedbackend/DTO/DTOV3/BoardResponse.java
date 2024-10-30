package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Visibilities;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class BoardResponse {
    private String id;
    private String name;
    private Visibilities visibilities;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
    private Users users;
    private List<Collab> collab;
}
