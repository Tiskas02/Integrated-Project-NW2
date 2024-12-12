package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.BoardResponse;
import com.example.integratedbackend.DTO.DTOV3.CollabDTO;
import com.example.integratedbackend.DTO.DTOV3.NewBoardDTO;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.TaskNameDuplicatedException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Visibilities;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.CollabRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import io.viascom.nanoid.NanoId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    private CollabRepositoriesV3 collabRepositoriesV3;
    @Autowired
    private UsersRepositoriesV3 usersRepositoriesV3;
    @Autowired
    private StatusServiceV3 statusServiceV3;
    @Autowired
    ModelMapper modelMapper;

    private CollabDTO convertToCollabDTO(Collab collab) {
        CollabDTO dto = new CollabDTO();
        dto.setOid(collab.getUserId());
        dto.setBoardId(collab.getBoardId());
        dto.setBoardName(collab.getBoard().getName());
        dto.setOwnerName(collab.getBoard().getUsers().getName());
        dto.setName(collab.getBoard().getName());
        dto.setEmail(collab.getBoard().getUsers().getEmail());
        dto.setAccessRight(collab.getAccessRight());
        dto.setStatus((collab.getStatus()));
        dto.setAddedOn(collab.getAddedOn());
        return dto;
    }
public List<BoardResponse> getBoardByUserName(String userName) {
    List<BoardResponse> boardResponses = new ArrayList<>();
    List<Users> users = usersRepositoriesV3.findAllByUsername(userName);

    if (users.isEmpty()) {
        throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "User with username " + userName + " not found");
    }

    Users user = users.get(0);

    List<Collab> collabList = collabRepositoriesV3.findCollabByUserId(user.getOid());
    List<CollabDTO> collabDTOList = collabList.stream().map(this::convertToCollabDTO).collect(Collectors.toList());

    List<Boards> boards = boardsRepositoriesV3.findBoardsByUsersOid(user.getOid());

    if (boards.isEmpty()) {
        BoardResponse placeholderResponse = new BoardResponse();
        placeholderResponse.setCollabIn(collabDTOList);
        boardResponses.add(placeholderResponse);
    } else {
        for (Boards board : boards) {
            BoardResponse boardResponse = new BoardResponse();
            boardResponse.setId(board.getId());
            boardResponse.setName(board.getName());
            boardResponse.setVisibilities(board.getVisibility());
            boardResponse.setUsers(board.getUsers().getName());
            boardResponse.setCreatedOn(board.getCreatedOn());
            boardResponse.setUpdatedOn(board.getUpdatedOn());
            boardResponse.setCollabIn(collabDTOList);

            boardResponses.add(boardResponse);
        }
    }

    return boardResponses;
}


    public Boards getBoardByBoardId(String boardId) {
        Optional<Boards> boards = boardsRepositoriesV3.findById(boardId);
            if (boards.isEmpty()) {
                throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board with id " +
                        boardId + " not found");
            }
        return boards.get();
    }

    public Boards createBoard(String userName, NewBoardDTO boardDTO) {
        List<Users> listUsers = usersRepositoriesV3.findAllByUsername(userName);
        Users user = listUsers.get(0);
        Users users = usersRepositoriesV3.findById(user.getOid()).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.FORBIDDEN, "User with ID " + user + " not found"));
        if (boardsRepositoriesV3.existsBoardsByNameIgnoreCaseAndUsers(boardDTO.getName(), users)) {
            throw new TaskNameDuplicatedException("must be unique");
        }
        String boardId = NanoId.generate(10);
        Boards newBoard = modelMapper.map(new Boards(), Boards.class);
        newBoard.setId(boardId);
        newBoard.setName(boardDTO.getName());
        newBoard.setUsers(user);
        Boards savedBoard = boardsRepositoriesV3.save(newBoard);
        statusServiceV3.createDefaultStatus(savedBoard.getId());
        return savedBoard;
    }

    public Boards updateBoard(String boardId, Boards boardDTO) {
        // Fetch the existing board
        Optional<Boards> existingBoardOpt = boardsRepositoriesV3.findById(boardId);
        if (existingBoardOpt.isEmpty()) {
            throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board with id " + boardId + " not found");
        }

        Boards existingBoard = existingBoardOpt.get();

        existingBoard.setName(boardDTO.getName());

        if (boardDTO.getId() == null){
            existingBoard.setId(boardId);
        }
        if (boardDTO.getVisibility() == null) {
            existingBoard.setVisibility(existingBoard.getVisibility());
        }
        if (boardDTO.getCreatedOn() == null){
            existingBoard.setCreatedOn(existingBoard.getCreatedOn());
        }
        if (boardDTO.getUpdatedOn() == null){
            existingBoard.setUpdatedOn(existingBoard.getUpdatedOn());
        }
        if (boardDTO.getCollab() == null) {
            existingBoard.setCollab(existingBoard.getCollab());
        }
        if (boardDTO.getUsers() == null){
            existingBoard.setUsers(existingBoard.getUsers());
        }

        return boardsRepositoriesV3.save(existingBoard);
    }


    @Transactional
    public Boards deleteBoardByBoardId(String boardId) {
        Optional<Boards> board = boardsRepositoriesV3.findById(boardId);
        if (board.isEmpty()) {
            throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board with id " + boardId + " not found");
        }

        boardsRepositoriesV3.deleteStatusesByBoardId(boardId);


        boardsRepositoriesV3.deleteBoardById(boardId);

        return board.get();
    }


    public boolean boardExists(String boardId) {
        if (boardId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Board Found");
        }
        return boardsRepositoriesV3.existsById(boardId);
    }

    public boolean isBoardPublic(String boardId) {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
        return boards.getVisibility().equals(Visibilities.PUBLIC);
    }

    public boolean isBoardOwner(String boardId, String oid) {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
            return boards.getUsers().getOid().equals(oid);
    }

}



