package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Visibilities;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisibilityService {
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    ModelMapper modelMapper;

    public boolean checkVisibility(String boardId) {
        //เช็คว่ามีบอร์ดมั้ย
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("Board not found"));

        //ถ้าvisibility = private return true, other return false
        return boards.getVisibilities() == Visibilities.PRIVATE;
    }
}
