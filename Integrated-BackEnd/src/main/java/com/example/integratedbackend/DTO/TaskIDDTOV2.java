package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import com.example.integratedbackend.Entities.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class TaskIDDTOV2 {
    private int taskId;
    private String Title;
    private String Assignees;
    private String Description;
    private Status Status;
//       @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
//    private ZonedDateTime createdOn;
//       @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
//    private ZonedDateTime updatedOn;
}
