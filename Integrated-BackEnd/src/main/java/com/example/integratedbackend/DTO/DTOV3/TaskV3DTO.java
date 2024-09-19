package com.example.integratedbackend.DTO.DTOV3;

import lombok.Data;

@Data
public class TaskV3DTO {
    private Integer id;
    private String title;
    private String Assignees;
    private Integer status;
    private String boardId;
}
