package com.example.integratedbackend.Controller.ControllerV3;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.DTO.DTOV3.*;
import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.*;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.ServiceV3.*;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("v3/boards")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th", "*"})
public class BoardControllerV3 {
    @Autowired
    private BoardService boardService;
    @Autowired
    private StatusServiceV3 statusServiceV3;
    @Autowired
    private VisibilityService visibilityService;
    @Autowired
    private TaskServiceV3 taskServiceV3;
    @Autowired
    private CollabService collabService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private UserServiceV3 userServiceV3;

    // ================================Board=====================================
    @GetMapping("")
    public ResponseEntity<Object> getBoardByUserId(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);
            if (username != null) {
                List<Boards> boards = boardService.getBoardByUserId(username);
                return new ResponseEntity<>(boards, HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("/{boardId}/{collabId}")
    public ResponseEntity<Object> getBoardByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable(required = false) String collabId) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();  // Board owner's username

        Collab collabAccess = collabService.getCollaborator(boardId, collabId);

        //if owner ให้เข้าเลย
        if (usernameFromToken.equals(boardOwnerName)) {
            return new ResponseEntity<>(boardInfo, HttpStatus.OK);
        }

        // Check board visibility
        boolean visibleValue = visibilityService.checkVisibility(boardId);
        if (!visibleValue) {
                if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                    if (usernameFromToken != null) {
                        Boards boards = boardService.getBoardByBoardId(boardId);
                        return new ResponseEntity<>(boards, HttpStatus.OK);
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                    }
                }
            }

            if (visibleValue) {
                if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                    if (usernameFromToken != null) {
                        Boards boards = boardService.getBoardByBoardId(boardId);
                        return new ResponseEntity<>(boards, HttpStatus.OK);
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                    }
                }

            }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }


    @PostMapping("")
    public ResponseEntity<Object> createBoard(
            @RequestHeader(value = "Authorization") String authHeader,
            @RequestBody @Valid NewBoardDTO newBoard) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractClaim(jwt, Claims::getSubject);
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
        if (newBoard == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Board name cannot be empty");
        }
        if (newBoard.getName() == null || newBoard.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Board creation failed. Name is required.");
        }
        Boards board = boardService.createBoard(username, newBoard);
        if (board.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Board creation failed. Please check the input data.");
        }
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    // ================================statuses=====================================
    @GetMapping("{boardId}/statuses/{collabId}")
    public ResponseEntity<Object> getAllStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable String collabId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            Boards boardInfo = boardService.getBoardByBoardId(boardId);
            String boardOwnerName = boardInfo.getUsers().getUsername();  // Board owner's username

            Collab collabAccess = collabService.getCollaborator(boardId, collabId);

            //if owner ให้เข้าเลย
            if (usernameFromToken.equals(boardOwnerName)) {
                return new ResponseEntity<>(boardInfo, HttpStatus.OK);
            }


            if (!visibleValue) {
                if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                    if (usernameFromToken != null) {
                        if (boardService.getBoardByBoardId(boardId) != null) {
                            return ResponseEntity.ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId), StatusDTO.class, modelMapper));
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                    }
                }
            }

            if (visibleValue) {
                if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                    if (usernameFromToken != null) {
                        if (boardService.getBoardByBoardId(boardId) != null) {
                            return ResponseEntity.ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId), StatusDTO.class, modelMapper));
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                    }
                }
                }

        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("{boardId}/statuses/{statusId}/{collabId}")
    public ResponseEntity<Object> findStatusById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable Integer statusId,
            @PathVariable String collabId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            Boards boardInfo = boardService.getBoardByBoardId(boardId);
            String boardOwnerName = boardInfo.getUsers().getUsername();  // Board owner's username

            Collab collabAccess = collabService.getCollaborator(boardId, collabId);

            //if owner ให้เข้าเลย
            if (usernameFromToken.equals(boardOwnerName)) {
                return new ResponseEntity<>(boardInfo, HttpStatus.OK);
            }

            if (!visibleValue) {
                if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                    if (usernameFromToken != null) {
                        return ResponseEntity.ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                    }
                }
            }

            if (visibleValue) {
                if (usernameFromToken != null) {
                    return ResponseEntity.ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @PostMapping("/{boardId}/statuses")
    public ResponseEntity<Object> createStatus(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody NewStatusDTO statusV3,
            @PathVariable String boardId) {
        StatusV3 createdStatus = statusServiceV3.createStatus(statusV3, boardId);
        StatusDTO statusDTO = modelMapper.map(createdStatus, StatusDTO.class);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                return new ResponseEntity<>(statusDTO, HttpStatus.CREATED);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @PutMapping("/{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> updateStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int statusId,
            @Valid @RequestBody NewStatusDTO status,
            @PathVariable String boardId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                boardService.getBoardByBoardId(boardId);
                return new ResponseEntity<>(modelMapper.map(statusServiceV3.updateStatus(statusId, status), StatusDtoV3.class), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @DeleteMapping("/{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> deleteStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer statusId,
            @PathVariable String boardId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                return new ResponseEntity<>(statusServiceV3.deleteStatus(statusId), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @DeleteMapping("/{boardId}/statuses/{statusId}/{newId}")
    public ResponseEntity<Object> transferStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer statusId,
            @PathVariable Integer newId,
            @PathVariable String boardId) throws BadRequestException {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                return new ResponseEntity<>(statusServiceV3.transferStatus(statusId, newId), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("/{boardId}/statuses/{statusId}/indicator")
    public ResponseEntity<Boolean> checkDeleteOrTransfer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer statusId,
            @PathVariable String boardId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                return ResponseEntity.ok(statusServiceV3.deleteOrTransfer(statusId));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    // ================================Task=====================================
    @GetMapping("{boardId}/tasks/{collabId}")
    public ResponseEntity<Object> getAllTaskByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable String collabId,
            @RequestParam(required = false) List<String> filterStatuses,
            @RequestParam(required = false, defaultValue = "") String[] sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String[] sortDirection
    ) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            Boards boardInfo = boardService.getBoardByBoardId(boardId);
            String boardOwnerName = boardInfo.getUsers().getUsername();  // Board owner's username

            Collab collabAccess = collabService.getCollaborator(boardId, collabId);

            //if owner ให้เข้าเลย
            if (usernameFromToken.equals(boardOwnerName)) {
                return new ResponseEntity<>(boardInfo, HttpStatus.OK);
            }

            if (!visibleValue) {
                if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                    if (usernameFromToken != null) {
                        return ResponseEntity.ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId));
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                    }
                }
            }

            if (visibleValue) {
                if (usernameFromToken != null) {
                    return ResponseEntity.ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("{boardId}/tasks/{id}/{collabId}")
    public ResponseEntity<Object> findTaskByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @PathVariable String boardId,
            @PathVariable String collabId) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            Boards boardInfo = boardService.getBoardByBoardId(boardId);
            String boardOwnerName = boardInfo.getUsers().getUsername();  // Board owner's username

            Collab collabAccess = collabService.getCollaborator(boardId, collabId);

            //if owner ให้เข้าเลย
            if (usernameFromToken.equals(boardOwnerName)) {
                return new ResponseEntity<>(boardInfo, HttpStatus.OK);
            }

            if (!visibleValue) {
                if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                    if (usernameFromToken != null) {
                        TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
                        return ResponseEntity.ok(modelMapper.map(task, TaskDTOV3.class));
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                    }
                }
            }

            if (visibleValue) {
                if (usernameFromToken != null) {
                    TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
                    return ResponseEntity.ok(modelMapper.map(task, TaskDTOV3.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @PostMapping("/{boardId}/tasks")
    public ResponseEntity<Object> createTask(
            @Valid @RequestBody NewTaskDTOV3 newTask,
            @PathVariable String boardId,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (username != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(taskServiceV3.createTask(newTask, boardId), NewTaskReturnV3.class));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @DeleteMapping("/{boardId}/tasks/{id}")
    public TaskDTOV3 deleteTask(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @PathVariable String boardId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (username != null) {
                return taskServiceV3.deleteTask(id, boardId);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @PutMapping("/{boardId}/tasks/{id}")
    public ResponseEntity<Object> updateTask(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody NewTaskDTOV3 editTask,
            @PathVariable String boardId,
            @PathVariable Integer id) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (username != null) {
                TaskV3 editedTask = taskServiceV3.updateTask(editTask, id, boardId);
                return ResponseEntity.ok(editedTask);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
//        return ResponseEntity.ok(modelMapper.map(taskServiceV3.updateTask(editTask, id), TaskIDDTOV2.class));
    }

    // ================================Visibilities=====================================

    @PatchMapping("/{boardId}")
    public ResponseEntity<Object> updateBoard(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @RequestBody VisibilityDTO newVisibility) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);
            if (username != null) {
                String updatedVisibility = visibilityService.changeVisibility(boardId, newVisibility);
                return ResponseEntity.ok(updatedVisibility);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    // ================================Collaborator=====================================

    @GetMapping("/{boardId}/collabs")
    public ResponseEntity<List<Collab>> getAllCollabs(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                List<Collab> collabs = collabService.getAllCollaborator(boardId);
                return ResponseEntity.ok(collabs);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("/{boardId}/collab/{callabId}")
    public ResponseEntity<Collab> getCollab(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable String callabId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                Collab collab = collabService.getCollaborator(boardId, callabId);
                return ResponseEntity.ok(collab);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("/collab/{collabId}")
    public ResponseEntity<CollabBoardResponse> getCollabBoard(@PathVariable String collabId) {
        CollabBoardResponse response = collabService.getCollabBoard(collabId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{boardId}/collabs")
    public ResponseEntity<Collab> addCollab(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @RequestBody CollabRequestDTO collabRequestDTO) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                Collab collab = collabService.addCollaborator(boardId, collabRequestDTO);
                return new ResponseEntity<>(collab, HttpStatus.CREATED);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @DeleteMapping("/{boardId}/collab/{callabId}")
    public ResponseEntity<Collab> deleteCollab(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable String callabId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                Collab collab = collabService.deleteCollaborator(boardId, callabId);
                return ResponseEntity.ok(collab);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }
}

