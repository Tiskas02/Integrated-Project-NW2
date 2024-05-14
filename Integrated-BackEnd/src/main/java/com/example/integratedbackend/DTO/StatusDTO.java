package com.example.integratedbackend.DTO;

import lombok.Data;

@Data
public class StatusDTO {
    private Integer Id;
    private String name;
    private String description;

    public void setName(String name) {
        this.name = (name != null) ? name.trim() : null;
    }
    public void setDescription(String description) {
        this.description = (description != null) ? description.trim() : null;
    }
}