package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import lombok.Data;

import java.util.List;

@Data
public class boardAndCollabReturn {
    private List<Boards> boards;
    private List<Collab> collaborators;
}
