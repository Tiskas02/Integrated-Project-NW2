package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.CollabBoardResponse;
import com.example.integratedbackend.DTO.DTOV3.CollabRequestDTO;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.CollabRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import com.example.integratedbackend.Users.User;
import com.example.integratedbackend.Users.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CollabService {
    @Autowired
    private CollabRepositoriesV3 collabRepositoriesV3;
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private UsersRepositoriesV3 usersRepositoriesV3;

    public List<Collab> getAllCollaborator(String boardId){
        return collabRepositoriesV3.findByBoardId(boardId);
    }

    public Collab getCollaborator(String boardId, String userId){
        return collabRepositoriesV3.findByBoardIdAndUserId(boardId, userId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Collaborator not found"));
    }

//    public List<Collab> getCollabBoard (String boardId){
//        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
//                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));
//
//        List<Collab> collab = collabRepositoriesV3.findByBoardId(boardId);
//
//        if (collab.isEmpty()){
//            throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "No collaborators found for this board");
//        }
//
//        return collab;
//    }

    public CollabBoardResponse getCollabBoard (String userId){
        userRepository.findById(userId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "User not found"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "User not found"));
        String username = user.getUsername();

        List<Collab> collabs = collabRepositoriesV3.findByUserId(userId);

//        return collabs;
        return new CollabBoardResponse(username, collabs);
    }


    public Collab addCollaborator(String boardId, CollabRequestDTO collabRequestDTO){
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));
        User user = userRepository.findByEmail(collabRequestDTO.getEmail()).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "User not found"));

        Collab collab = modelMapper.map(collabRequestDTO, Collab.class);
        collab.setBoardId(boardId);
        collab.setUserId(user.getOid());
        collab.setAccessRight(collabRequestDTO.getAccessRight());
        collabRepositoriesV3.save(collab);

        return collab;
    }

    public Collab deleteCollaborator(String boardId, String userId){
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));
        Users users = usersRepositoriesV3.findById(userId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "User not found"));

        Collab collaboratorToDelete = collabRepositoriesV3.findByBoardIdAndUserId(boardId, userId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Collaborator not found"));
        collabRepositoriesV3.delete(collaboratorToDelete);
        return modelMapper.map(collaboratorToDelete, Collab.class);
    }
}
