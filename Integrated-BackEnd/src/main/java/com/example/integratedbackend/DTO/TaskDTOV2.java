package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.Status;
import lombok.*;

@Data
public class TaskDTOV2 {
    private Integer taskId;
    private String Title;
    private String Assignees;
    private Status status;
}
