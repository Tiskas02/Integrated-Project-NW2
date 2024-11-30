package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.*;
import com.example.integratedbackend.ErrorHandle.CollaboratorExistException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.NonCollaboratorException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.*;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.CollabRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import com.example.integratedbackend.Service.UserService;
import com.example.integratedbackend.Users.User;
import com.example.integratedbackend.Users.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollabService {
    @Autowired
    private CollabRepositoriesV3 collabRepositoriesV3;
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private UsersRepositoriesV3 usersRepositoriesV3;


    private CollabDTO convertToCollabDTO(Collab collab) {
        User user = userService.getUserById(collab.getUserId());
        CollabDTO collabDTO = new CollabDTO();

        collabDTO.setOid(collab.getUserId());
        collabDTO.setOwnerName(collab.getBoard().getUsers().getName());
        collabDTO.setAccessRight(collab.getAccessRight());
        collabDTO.setAddedOn(collab.getAddedOn());
        collabDTO.setBoardId(collab.getBoardId());
        collabDTO.setName(user.getName());
        collabDTO.setEmail(user.getEmail());

        return collabDTO;
    }

    public List<CollabDTO> getAllCollaborator(String boardId, String userId) {
        Boards board = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));

        if (userId.equals(board.getUsers().getOid())) {
            return mapCollabsToDTOs(collabRepositoriesV3.findByBoardId(boardId));
        }

        if (board.getVisibility() == Visibilities.PRIVATE) {
            boolean isCollaborator = collabRepositoriesV3.existsByBoardIdAndUserId(boardId, userId);

            if (!isCollaborator) {
                throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Access Denied");
            }
        }

        return mapCollabsToDTOs(collabRepositoriesV3.findByBoardId(boardId));
    }

    private List<CollabDTO> mapCollabsToDTOs(List<Collab> collabs) {
        return collabs.stream()
                .map(this::convertToCollabDTO)
                .collect(Collectors.toList());
    }


    public AccessRight getCollabRight(String userId) {
        List<Collab> userCollabs = collabRepositoriesV3.findByUserId(userId);

        if (userCollabs == null || userCollabs.isEmpty()) {
            return null;
        }

        return userCollabs.get(0).getAccessRight();
    }

    public accessRightDTO changeAccessRight(String boardId, String userId, String collabId, accessRightDTO newAccessRight) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }

        boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));

        Collab collab = collabRepositoriesV3.findByBoardIdAndUserId(boardId, collabId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Collab not found"));

        if (collab.getAccessRight() != AccessRight.READ && collab.getAccessRight() != AccessRight.WRITE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Denied");
        }

        collab.setAccessRight(newAccessRight.getAccessRight());
        collabRepositoriesV3.save(collab);
        return modelMapper.map(collab, accessRightDTO.class);

    }

    public CollabDTO getCollaborator(String boardId, String userId) {
        Boards board = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));
        Collab collab = collabRepositoriesV3.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new NonCollaboratorException(HttpStatus.FORBIDDEN, "Collaborator not found"));

        return convertToCollabDTO(collab);
    }

    public Collab getCollab(String boardId, String userId) {
        Boards board = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));
        Collab collab = collabRepositoriesV3.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new NonCollaboratorException(HttpStatus.FORBIDDEN, "Collaborator not found"));

        return collab;
    }

    public CollabDTO getCollaboratorOfBoard(String boardId, String collabId, String userId) {
        Boards board = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));

        if (board.getVisibility() == Visibilities.PRIVATE) {
            if (userId.equals(board.getUsers().getOid())) {
                Collab collab = collabRepositoriesV3.findByBoardIdAndUserId(boardId, collabId)
                        .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Collaborator not found"));
                return convertToCollabDTO(collab);
            }

            boolean isCollaborator = collabRepositoriesV3.existsByBoardIdAndUserId(boardId, userId);
            if (!isCollaborator) {
                throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Access Denied");
            }
        }
        Collab collab = collabRepositoriesV3.findByBoardIdAndUserId(boardId, collabId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Collaborator not found"));

        return convertToCollabDTO(collab);
    }


    public CollabBoardResponse getCollabBoard(String userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "User not found"));

        List<Collab> collabs = collabRepositoriesV3.findByUserId(userId);

        List<String> boardIds = collabs.stream()
                .map(Collab::getBoardId)
                .collect(Collectors.toList());

        List<Boards> boards = boardsRepositoriesV3.findByIdIn(boardIds);

        Map<String, Boards> boardIdToBoardMap = boards.stream()
                .collect(Collectors.toMap(Boards::getId, board -> board));

        List<CollabBoardDto> collabBoardDtos = collabs.stream()
                .map(collab -> {
                    Boards board = boardIdToBoardMap.get(collab.getBoardId());
                    return new CollabBoardDto(
                            collab.getBoardId(),
                            board.getName(),
                            board.getUsers().getName(),
                            userId,
                            collab.getAddedOn(),
                            collab.getAccessRight()
                    );
                })
                .collect(Collectors.toList());

        return new CollabBoardResponse(collabBoardDtos);
    }

    public CollabDTO addCollaborator(String userId, String boardId, CollabRequestDTO collabRequestDTO) {
        Boards boards = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));

        if (collabRequestDTO.getAccessRight() == null || collabRequestDTO.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access right is required");
        }

        User user = userService.getUserByEmail(collabRequestDTO.getEmail());
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id is needed");
        } else if (user.getOid().equals(userId)) {
            throw new CollaboratorExistException(HttpStatus.CONFLICT, "Board owner cannot be collaborator");
        }

        if (collabRepositoriesV3.findByBoardIdAndUserId(boardId, user.getOid()).isPresent()) {
            throw new CollaboratorExistException(HttpStatus.CONFLICT, "Collaborator already exists");
        }

        Collab collab = modelMapper.map(collabRequestDTO, Collab.class);
        collab.setBoardId(boardId);
        collab.setUserId(user.getOid());

        collab.setAccessRight(collabRequestDTO.getAccessRight());
        collabRepositoriesV3.save(collab);

        CollabDTO collabDTO = new CollabDTO();
        collabDTO.setOid(collab.getUserId());
        collabDTO.setBoardId(collab.getBoardId());
        collabDTO.setName(user.getName());
        collabDTO.setEmail(user.getEmail());
        collabDTO.setAccessRight(collab.getAccessRight());
        collabDTO.setAddedOn(collab.getAddedOn());

        return collabDTO;
    }

//    @Transactional
//    public Collab deleteCollaborator(String boardId, String userId) {
//        boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
//                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));
//        usersRepositoriesV3.findById(userId).orElseThrow(() ->
//                new ItemNotFoundException(HttpStatus.NOT_FOUND, "User not found"));
//
//        Collab collaboratorToDelete = collabRepositoriesV3.getByBoardIdAndUserId(boardId, userId);
//        if (collaboratorToDelete == null) {
//            throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Collaborator not found");
//        }
//
//        collabRepositoriesV3.delete(collaboratorToDelete);
//
//        //check existing collab after deleted
//        boolean existsAfterDelete = collabRepositoriesV3.findByBoardIdAndUserId(boardId, userId).isPresent();
//        if (existsAfterDelete) {
//            throw new RuntimeException("Deletion failed: Collaborator still exists");
//        }
//        return modelMapper.map(collaboratorToDelete, Collab.class);
//    }

    @Transactional
    public Collab deleteCollaborator(String boardId, String userId) {
        Boards board = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board not found"));
        Collab collab = collabRepositoriesV3.getCollabByBoardIdAndUserId(boardId, userId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.NOT_FOUND, "Collaborator not found"));

        collabRepositoriesV3.deleteCollabByBoardIdAndUserId(collab.getBoardId(), collab.getUserId());
        collabRepositoriesV3.flush();

        return collab;
    }

    public boolean isCollaborator(String boardId, String userId) {
        Optional<Collab> collab = collabRepositoriesV3.findByBoardIdAndUserId(boardId, userId);
        return collab.isPresent();
    }

    public boolean isWriteAccess(String boardId, String userId) {
        return collabRepositoriesV3.existsByBoardIdAndUserIdAndAccessRight(boardId, userId, AccessRight.WRITE);
    }
}
