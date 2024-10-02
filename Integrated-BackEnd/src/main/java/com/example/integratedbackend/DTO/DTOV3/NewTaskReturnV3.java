package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class NewTaskReturnV3 {
    private Integer id;
    private String title;
    private String assignees;
    private String description;
    private StatusV3 status;
}
