package com.example.integratedbackend.DTO;

import lombok.Data;



@Data
public class NewTaskDTOV2 {
    private Integer taskId;
    private String Title;
    private String Assignees;
    private String Description;
    private Integer statusId;
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