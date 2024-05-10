package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import com.example.integratedbackend.Entities.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class TaskIDDTOV2 {
    private Integer taskId;
    private String Title;
    private String Assignees;
    private String Description;
    private Status Status;

}
