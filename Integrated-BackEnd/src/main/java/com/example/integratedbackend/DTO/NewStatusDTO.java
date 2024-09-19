package com.example.integratedbackend.DTO;

import jakarta.validation.constraints.NotBlank;
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
}
