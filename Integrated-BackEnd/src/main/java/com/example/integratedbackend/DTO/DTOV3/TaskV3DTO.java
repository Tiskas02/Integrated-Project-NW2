package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import lombok.Data;

@Data
public class TaskV3DTO {
    private Integer id;
    private String title;
    private String Assignees;
    private Integer status;
    private String boardId;
}
