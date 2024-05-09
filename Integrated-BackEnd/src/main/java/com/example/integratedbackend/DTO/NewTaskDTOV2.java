package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import com.example.integratedbackend.Entities.Status;
import lombok.Data;

import java.net.Inet4Address;

@Data
public class NewTaskDTOV2 {
    private Integer taskId;
    private String Title;
    private String Assignees;
    private String Description;
    private Status Status;
    public void setDescription(String description) {
        if(description != null) {
            this.Description = description.trim();
        }
    }
    public void setAssignees(String assignees) {
        this.Assignees = (assignees != null) ? assignees.trim() : null;
    }

    public void setTitle(String title) {
        this.Title = (title != null) ? title.trim() : null;
    }

}
