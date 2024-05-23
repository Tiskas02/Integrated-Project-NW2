package com.example.integratedbackend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewStatusDTO {
    @NotBlank(message = "must not be null")
    @Size(max = 50, message = "Status name cannot more than 50 character")
    private String name;
    @Size(max = 200, message = "Status description cannot more than 200 character")
    private String description;

    public void setName(String name) {
        if (name == null) {
            return;
        }
        this.name = name.trim();
        if (this.name.isEmpty()) {
            this.name = null;
        }
    }

    public void setDescription(String description) {
        if (description == null) {
            return;
        }
        this.description = description.trim();
        if (this.description.isEmpty()) {
            this.description = null;
        }
    }

    @Data
    public static class NewTaskDTOV2 {
        private Integer id;
        @NotBlank(message = "must not be null")
        @Size(max = 100, message = "Title size must be between 1 and 100")
        private String Title;
        @Size(max = 30, message = "Assignees size must be up to 30 characters")
        private String Assignees;
        @Size(max = 500, message = "Description size must be between 1 and 500")
        private String Description;
        @NotNull
        private Integer statusId;

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
            this.Title = (title != null) ? title.trim() : null;
        }

    }
}
