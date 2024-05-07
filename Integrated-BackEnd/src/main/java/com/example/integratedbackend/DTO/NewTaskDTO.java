package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import lombok.Data;

@Data
public class NewTaskDTO {
    private String Title;
    private String Assignees;
    private String Description;
    private ResourceType Status;
    public void setDescription(String description) {
        if(description != null) {
            this.Description = description.trim();
        }
    }
//    public void setAssignees(String assignees) {
//        if(assignees != null) {
//            this.Assignees = assignees.trim();
//        }
//    }
//    public void setTitle(String title) {
//        if(title != null) {
//            this.Title = title.trim();
//        }
//    }
//    public void setStatus(ResourceType status) {
//        if(status == null ) {
//            this.Status = ResourceType.NO_STATUS;
//        }
//    }
}
