package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import lombok.Data;

@Data
public class NewTaskDTO {
    private String Title;
    private String Assignees;
    private String Description;
    private ResourceType Status;
}