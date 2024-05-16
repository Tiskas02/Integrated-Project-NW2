package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class TaskIDDTOV2 {
    private Integer id;
    private String Title;
    private String Assignees;
    private String Description;
    private Status Status;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}