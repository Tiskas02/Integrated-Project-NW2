package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class TaskIDDTO {
    private int taskId;
    private String Title;
    private String Assignees;
    private String Description;
    private ResourceType Status;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}