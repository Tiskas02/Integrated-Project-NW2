package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import lombok.Data;

@Data
public class NewTaskDTOV2 {
    private String Title;
    private String Assignees;
    private String Description;
    private ResourceType Status;
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

    public void setStatus(ResourceType status) {
        this.Status = (status != null) ? status : ResourceType.NO_STATUS;
    }

}
