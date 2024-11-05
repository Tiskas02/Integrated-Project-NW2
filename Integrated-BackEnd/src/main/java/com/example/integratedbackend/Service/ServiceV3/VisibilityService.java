package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.VisibilityDTO;
import com.example.integratedbackend.ErrorHandle.AccessRightNotAllow;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.NonCollaboratorException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class VisibilityService {
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    private CollabService collabService;
    @Autowired
    ModelMapper modelMapper;

    public boolean checkVisibility(String boardId) {
        //เช็คว่ามีบอร์ดมั้ย
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("Board not found"));

        //ถ้าvisibility = private return true, other return false
//        return boards.getVisibilities() == Visibilities.PRIVATE;
        return "private".equals(boards.getVisibility());
    }

    public VisibilityDTO changeVisibility(String boardId, VisibilityDTO newVisibilities, String userId) {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));

        if (userId.equals(boards.getUsers().getOid())) {
            boards.setVisibility(newVisibilities.getVisibility());
            boardsRepositoriesV3.save(boards);
            return modelMapper.map(boards, VisibilityDTO.class);
        }

        AccessRight accessRight = collabService.getCollabRight(userId);
        if (accessRight == null) {
            throw new AccessRightNotAllow(HttpStatus.FORBIDDEN, "You are not allowed to update this board");
        }

        if (accessRight == AccessRight.READ) {
            throw new AccessRightNotAllow(HttpStatus.FORBIDDEN, "Collaborators with READ access cannot update the board");
        }

        boards.setVisibility(newVisibilities.getVisibility());
        boardsRepositoriesV3.save(boards);
        return modelMapper.map(boards, VisibilityDTO.class);
    }


}
