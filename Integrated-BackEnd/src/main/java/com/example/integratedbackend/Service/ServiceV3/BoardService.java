package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.NewBoardDTO;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.TaskNameDuplicatedException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import io.viascom.nanoid.NanoId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    private UsersRepositoriesV3 usersRepositoriesV3;
    @Autowired
    private StatusServiceV3 statusServiceV3;
    @Autowired
    ModelMapper modelMapper;


    public List<Boards> getBoardByUserId(String userName) {
        List<Users> users = usersRepositoriesV3.findAllByUsername(userName);
        Users user = users.get(0);
        List<Boards> boards = boardsRepositoriesV3.findBoardsByUsersOid(user.getOid());
        if (boards == null) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board with username" +
                    " " + userName + " not found");
        }
        return boards;
    }

    public Boards getBoardByBoardId(String boardId) {
        Optional<Boards> boards = boardsRepositoriesV3.findById(boardId);
        if (boards.isEmpty()) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board with id " +
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
}



