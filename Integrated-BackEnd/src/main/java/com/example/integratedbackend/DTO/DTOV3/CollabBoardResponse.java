package com.example.integratedbackend.DTO.DTOV3;

import java.util.List;

public class CollabBoardResponse {
    private List<CollabBoardDto> boards;

    public CollabBoardResponse(List<CollabBoardDto> boards) {
        this.boards = boards;
    }

    public List<CollabBoardDto> getBoards() {
        return boards;
    }

    public void setBoards(List<CollabBoardDto> boards) {
        this.boards = boards;
    }
}


