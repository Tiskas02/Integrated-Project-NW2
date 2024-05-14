package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Entities.ResourceType;
import com.example.integratedbackend.Entities.Status;
import lombok.Data;

import java.net.Inet4Address;

@Data
public class NewTaskDTOV2 {
    private Integer id;
    private String Title;
    private String Assignees;
    private String Description;
    private Integer statusId;
    public void setDescription(String description) {
        if(description != null) {
            this.Description = description.trim();
            if (this.Description.isEmpty()){
                this.Description = null;
            }
        }else {
            return;
        }
    }
    public void setAssignees(String assignees) {
        this.Assignees = (assignees != null) ? assignees.trim() : null;
        if (assignees != null){
            this.Assignees = assignees.trim();
            if (this.Assignees.isEmpty()) {
                this.Assignees = null;
            }
        }else {
            return;
        }
    }

    public void setTitle(String title) {
        this.Title = (title != null) ? title.trim() : null;
    }

}
