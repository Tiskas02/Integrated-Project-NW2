package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Kradankanban.Status;
import lombok.*;

@Data
public class TaskDTOV2 {
    private Integer id;
    private String Title;
    private String Assignees;
    private Status status;
}
