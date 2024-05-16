package com.example.integratedbackend.DTO;

import lombok.Data;

@Data
public class NewStatusDTO {
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
