package com.example.integratedbackend.DTO.DTOV3;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class TaskDTOV3 {
    private Integer id;
    private String Title;
    private String description;
    private String Assignees;
    private StatusDtoV3 status;
    private BoardDto board;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}

