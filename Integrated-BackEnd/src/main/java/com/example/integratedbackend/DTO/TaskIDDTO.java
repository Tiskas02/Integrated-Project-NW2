package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Getter
@Setter
public class TaskIDDTO {
    private int id;
    private String Title;
    private String Assignees;
    private String Description;
    private ResourceType Status;
//    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime createdOn;
//    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime updatedOn;
}
