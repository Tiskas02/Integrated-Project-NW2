package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.TaskNameDuplicatedException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import com.example.integratedbackend.Service.ListMapper;
import io.viascom.nanoid.NanoId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    private UsersRepositoriesV3 usersRepositoriesV3;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public List<Boards> getBoardByUserId(String userName){
        List<Users> users = usersRepositoriesV3.findAllByUsername(userName);
        Users user = users.get(0);
        List<Boards> boards = boardsRepositoriesV3.findBoardsByUsersOid(user.getOid());
        if (boards == null){
            throw new ItemNotFoundException("Board with username" +
                    " " + userName + " not found");
        }
        return boards;
    }
    public Boards createBoard(String userName, String boardName){
        List<Users> listUsers = usersRepositoriesV3.findAllByUsername(userName);
        Users user = listUsers.get(0);
        Users users = usersRepositoriesV3.findById(user.getOid()).orElseThrow(() ->
            new ItemNotFoundException("User with ID " + user + " not found"));
        if(boardsRepositoriesV3.existsBoardsByNameIgnoreCaseAndUsers(boardName,users)){
            throw new TaskNameDuplicatedException("must be unique");
        }
//        if (getBoardByUserId(userName) != null){
//            throw new TaskNameDuplicatedException("User contain one board");
//        }
        String boardId = NanoId.generate(10);
        Boards newBoard = modelMapper.map(new Boards(), Boards.class);
        newBoard.setBoardId(boardId);
        newBoard.setName(boardName);
        newBoard.setUsers(user);
        Boards savedBoard = boardsRepositoriesV3.save(newBoard);
        return savedBoard;
    }

}
