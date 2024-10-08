package com.example.integratedbackend.DTO.DTOV3;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewTaskDTOV3 {
    private Integer id;
    @NotBlank(message = "must not be null")
    @Size(max = 100, message = "Title size must be between 1 and 100")
    private String title;
    @Size(max = 30, message = "Assignees size must be up to 30 characters")
    private String Assignees;
    @Size(max = 500, message = "Description size must be between 1 and 500")
    private String Description;
    @NotNull
    private Integer statusId;
    private Integer boardId;

    public void setDescription(String description) {
        if (description != null) {
            this.Description = description.trim();
            if (this.Description.isEmpty()) {
                this.Description = null;
            }
        }
    }

    public void setAssignees(String assignees) {
        if (assignees != null) {
            this.Assignees = assignees.trim();
            if (this.Assignees.isEmpty()) {
                this.Assignees = null;
            }
        }
    }
    public void setTitle(String title) {
        this.title = (title != null) ? title.trim() : null;
    }

}
