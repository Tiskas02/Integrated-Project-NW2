package com.example.integratedbackend.DTO.DTOV3;

import lombok.Data;

@Data
public class TaskDTOV3 {
    private Integer id;
    private String Title;
    private String description;
    private String Assignees;
    private StatusDtoV3 status;
    private BoardDto board;
}

