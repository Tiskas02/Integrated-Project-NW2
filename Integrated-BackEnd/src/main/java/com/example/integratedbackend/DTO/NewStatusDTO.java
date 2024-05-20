package com.example.integratedbackend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewStatusDTO {

    @NotBlank(message = "must not be null")
    private String name;
    private String description;

    public void setName(String name) {
        if (name == null){
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
