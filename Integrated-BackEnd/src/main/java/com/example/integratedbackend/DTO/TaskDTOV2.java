package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import com.example.integratedbackend.Entities.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Stack;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTOV2 {
    private Integer taskId;
    private String Title;
    private String Assignees;
    private Status Status;
}
