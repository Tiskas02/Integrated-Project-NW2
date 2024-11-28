package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class accessRightDTO {
    @NotNull
    private AccessRight accessRight;
}
