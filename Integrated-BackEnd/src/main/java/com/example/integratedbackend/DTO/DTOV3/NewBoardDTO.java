package com.example.integratedbackend.DTO.DTOV3;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewBoardDTO {
    @Size(max = 120, message = "name size must be between 1 and 120")
    private String name;
}
