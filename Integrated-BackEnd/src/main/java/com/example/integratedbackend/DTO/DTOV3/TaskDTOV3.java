package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.Status;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import lombok.Data;

@Data
public class TaskDTOV3 {
    private Integer id;
    private String Title;
    private String Assignees;
    private StatusV3 status;
    private Integer boardsId;
}
