package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CollabRequestDTO {
    @NotNull
    private String email;
    private AccessRight accessRight;
}
