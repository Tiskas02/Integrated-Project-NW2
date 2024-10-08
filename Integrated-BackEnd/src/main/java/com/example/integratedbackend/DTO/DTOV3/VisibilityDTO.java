package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Visibilities;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VisibilityDTO {
    @NotNull
    private Visibilities visibility;
}
