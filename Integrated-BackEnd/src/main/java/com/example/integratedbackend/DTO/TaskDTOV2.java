package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.StatusEntity;
import lombok.*;

import java.util.Stack;

@Data
public class TaskDTOV2 {
    private Integer taskId;
    private String Title;
    private String Assignees;
    private StatusEntity Status;
}
