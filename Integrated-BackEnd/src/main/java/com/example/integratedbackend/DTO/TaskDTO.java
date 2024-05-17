package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Integer id;
    private String Title;
    private String Assignees;
    private ResourceType Status;

}