package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import com.example.integratedbackend.Service.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public Boards getBoardByUserId(String userName){
        List<Users> users = usersRepositoriesV3.findAllByUsername(userName);
        Users user = users.get(0);
        Boards boards = boardsRepositoriesV3.getReferenceById(user.getOid());
        if (boards == null){
            throw new ItemNotFoundException("Board with username" +
                    " " + userName + " not found");
        }
        return boards;
    }
}
