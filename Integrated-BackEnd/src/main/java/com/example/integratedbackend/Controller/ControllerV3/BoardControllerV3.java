package com.example.integratedbackend.Controller.ControllerV3;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.DTO.DTOV3.*;
import com.example.integratedbackend.ErrorHandle.AccessRightNotAllow;
import com.example.integratedbackend.ErrorHandle.NonCollaboratorException;
import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.*;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.CollabRepositoriesV3;
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

import java.util.*;


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
    private CollabRepositoriesV3 collabRepositoriesV3;

    // ================================Board=====================================
    @GetMapping("")
    public ResponseEntity<Object> getBoardByUserId(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);
            if (username != null) {
                return ResponseEntity.ok(boardService.getBoardByUserName(username));
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<Object> getBoardByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // if owner ให้เข้าเลย
        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return new ResponseEntity<>(boardInfo, HttpStatus.OK);
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);

        // Check board visibility
        boolean visibleValue = visibilityService.checkVisibility(boardId);
        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    Boards boards = boardService.getBoardByBoardId(boardId);
                    return new ResponseEntity<>(boards, HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
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

        return new ResponseEntity<>(boardInfo, HttpStatus.OK);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Board creation failed. Please check the input data.");
        }
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    // ================================statuses=====================================

    @GetMapping("{boardId}/statuses")
    public ResponseEntity<Object> getAllStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // if owner ให้เข้าเลย
        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return ResponseEntity
                        .ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId), StatusDTO.class, modelMapper));
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    if (boardService.getBoardByBoardId(boardId) != null) {
                        return ResponseEntity.ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId),
                                StatusDTO.class, modelMapper));
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }

        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    if (boardService.getBoardByBoardId(boardId) != null) {
                        return ResponseEntity.ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId),
                                StatusDTO.class, modelMapper));
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        return ResponseEntity
                .ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId), StatusDTO.class, modelMapper));
    }

    @GetMapping("{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> findStatusById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable Integer statusId) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return ResponseEntity.ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return ResponseEntity
                            .ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }

        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return ResponseEntity
                            .ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        return ResponseEntity.ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
    }

    @PostMapping("/{boardId}/statuses")
    public ResponseEntity<Object> createStatus(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody NewStatusDTO statusV3,
            @PathVariable String boardId) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        if (usernameFromToken.equals(boardOwnerName)) {
            StatusV3 createdStatus = statusServiceV3.createStatus(statusV3, boardId);
            StatusDTO statusDTO = modelMapper.map(createdStatus, StatusDTO.class);
            return new ResponseEntity<>(statusDTO, HttpStatus.CREATED);
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        AccessRight accessRight = collabService.getCollabRight(userId);

        if (accessRight == AccessRight.READ) {
            throw new AccessRightNotAllow(HttpStatus.FORBIDDEN, "You don't have permission to create statuses.");
        }

        boolean isVisible = visibilityService.checkVisibility(boardId);

        if (!isVisible) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This board is private!");
            }
        } else if (isVisible) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This board is private!");
            }
        }

        StatusV3 createdStatus = statusServiceV3.createStatus(statusV3, boardId);
        StatusDTO statusDTO = modelMapper.map(createdStatus, StatusDTO.class);
        return new ResponseEntity<>(statusDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> updateStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int statusId,
            @Valid @RequestBody NewStatusDTO status,
            @PathVariable String boardId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                boardService.getBoardByBoardId(boardId);
                return new ResponseEntity<>(
                        modelMapper.map(statusServiceV3.updateStatus(statusId, status), StatusDtoV3.class),
                        HttpStatus.OK);
            }
        }
        AccessRight accessRight = collabService.getCollabRight(userId);
        if (accessRight == AccessRight.READ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION");
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    boardService.getBoardByBoardId(boardId);
                    return new ResponseEntity<>(
                            modelMapper.map(statusServiceV3.updateStatus(statusId, status), StatusDtoV3.class),
                            HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }
        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    boardService.getBoardByBoardId(boardId);
                    return new ResponseEntity<>(
                            modelMapper.map(statusServiceV3.updateStatus(statusId, status), StatusDtoV3.class),
                            HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        boardService.getBoardByBoardId(boardId);
        return new ResponseEntity<>(
                modelMapper.map(statusServiceV3.updateStatus(statusId, status), StatusDtoV3.class),
                HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> deleteStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer statusId,
            @PathVariable String boardId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return new ResponseEntity<>(statusServiceV3.deleteStatus(statusId), HttpStatus.OK);
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        AccessRight accessRight = collabService.getCollabRight(userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (accessRight == AccessRight.READ) {
            throw new AccessRightNotAllow(HttpStatus.FORBIDDEN, "YOU DON'T HAVE PERMISSION");
        }
        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return new ResponseEntity<>(statusServiceV3.deleteStatus(statusId), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }
        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return new ResponseEntity<>(statusServiceV3.deleteStatus(statusId), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        return new ResponseEntity<>(statusServiceV3.deleteStatus(statusId), HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}/statuses/{statusId}/{newId}")
    public ResponseEntity<Object> transferStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer statusId,
            @PathVariable Integer newId,
            @PathVariable String boardId) throws BadRequestException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return new ResponseEntity<>(statusServiceV3.transferStatus(statusId, newId), HttpStatus.OK);
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        AccessRight accessRight = collabService.getCollabRight(userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (accessRight == AccessRight.READ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION");
        }
        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return new ResponseEntity<>(statusServiceV3.transferStatus(statusId, newId), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }
        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return new ResponseEntity<>(statusServiceV3.transferStatus(statusId, newId), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        return new ResponseEntity<>(statusServiceV3.transferStatus(statusId, newId), HttpStatus.OK);
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

    @GetMapping("{boardId}/tasks")
    public ResponseEntity<Object> getAllTaskByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @RequestParam(required = false) List<String> filterStatuses,
            @RequestParam(required = false, defaultValue = "") String[] sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String[] sortDirection) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // if owner ให้เข้าเลย
        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return ResponseEntity
                        .ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId));
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        // Check board visibility
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return ResponseEntity
                            .ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }

        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return ResponseEntity
                            .ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        return ResponseEntity.ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId));
    }

    @GetMapping("{boardId}/tasks/{id}")
    public ResponseEntity<Object> findTaskByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @PathVariable String boardId) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // if owner ให้เข้าเลย
        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
                return ResponseEntity.ok(modelMapper.map(task, TaskDTOV3.class));
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
                    return ResponseEntity.ok(modelMapper.map(task, TaskDTOV3.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }

        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
                    return ResponseEntity.ok(modelMapper.map(task, TaskDTOV3.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
        return ResponseEntity.ok(modelMapper.map(task, TaskDTOV3.class));
    }

    @PostMapping("/{boardId}/tasks")
    public ResponseEntity<Object> createTask(
            @Valid @RequestBody NewTaskDTOV3 newTask,
            @PathVariable String boardId,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // if owner ให้เข้าเลย
        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(modelMapper.map(taskServiceV3.createTask(newTask, boardId), NewTaskReturnV3.class));
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        AccessRight accessRight = collabService.getCollabRight(userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (accessRight == AccessRight.READ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION");
        }
        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(modelMapper.map(taskServiceV3.createTask(newTask, boardId), NewTaskReturnV3.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }
        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(modelMapper.map(taskServiceV3.createTask(newTask, boardId), NewTaskReturnV3.class));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(taskServiceV3.createTask(newTask, boardId), NewTaskReturnV3.class));
    }

    @DeleteMapping("/{boardId}/tasks/{id}")
    public TaskDTOV3 deleteTask(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @PathVariable String boardId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // if owner ให้เข้าเลย
        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                return taskServiceV3.deleteTask(id, boardId);
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        AccessRight accessRight = collabService.getCollabRight(userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (accessRight == AccessRight.READ) {
            throw new AccessRightNotAllow(HttpStatus.FORBIDDEN, "YOU DON'T HAVE PERMISSION");
        }
        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return taskServiceV3.deleteTask(id, boardId);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }
        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    return taskServiceV3.deleteTask(id, boardId);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        return taskServiceV3.deleteTask(id, boardId);
    }

    @PutMapping("/{boardId}/tasks/{id}")
    public ResponseEntity<Object> updateTask(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody NewTaskDTOV3 editTask,
            @PathVariable String boardId,
            @PathVariable Integer id) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // if owner ให้เข้าเลย
        if (userId != null) {
            if (usernameFromToken.equals(boardOwnerName)) {
                TaskV3 editedTask = taskServiceV3.updateTask(editTask, id, boardId);
                return ResponseEntity.ok(editedTask);
            }
        }

        CollabDTO collabAccess = collabService.getCollaborator(boardId, userId);
        AccessRight accessRight = collabService.getCollabRight(userId);
        boolean visibleValue = visibilityService.checkVisibility(boardId);

        if (accessRight == AccessRight.READ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "YOU DON'T HAVE PERMISSION");
        }
        if (!visibleValue) {
            if (Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    TaskV3 editedTask = taskServiceV3.updateTask(editTask, id, boardId);
                    return ResponseEntity.ok(editedTask);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "YOU DON'T HAVE PERMISSION ON THIS BOARD");
                }
            }
        }
        if (visibleValue) {
            if (!Objects.equals(collabAccess.getBoardId(), boardId)) {
                if (usernameFromToken != null) {
                    TaskV3 editedTask = taskServiceV3.updateTask(editTask, id, boardId);
                    return ResponseEntity.ok(editedTask);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
                }
            }
        }
        TaskV3 editedTask = taskServiceV3.updateTask(editTask, id, boardId);
        return ResponseEntity.ok(editedTask);
    }

    // ================================Visibilities=====================================
    @PatchMapping("/{boardId}")
    public ResponseEntity<Object> updateBoard(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @RequestBody VisibilityDTO newVisibility) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        if (usernameFromToken.equals(boardOwnerName)) {
            VisibilityDTO updatedVisibility = visibilityService.changeVisibility(boardId, newVisibility, userId);
            return ResponseEntity.ok(updatedVisibility);
        }

        VisibilityDTO updatedVisibility = visibilityService.changeVisibility(boardId, newVisibility, userId);
        return ResponseEntity.ok(updatedVisibility);
    }

    // ================================Collaborator=====================================

    @GetMapping("/{boardId}/collabs")
    public ResponseEntity<List<CollabDTO>> getAllCollabs(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);
            String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

            Boards boardInfo = boardService.getBoardByBoardId(boardId);

            if (boardInfo != null && username != null) {
                String boardOwnerName = boardInfo.getUsers().getUsername();

                if (username.equals(boardOwnerName)) {
                    List<CollabDTO> collabDTOS = collabService.getAllCollaborator(boardId, userId);
                    return ResponseEntity.ok(collabDTOS);
                }

                List<CollabDTO> collabDTOS = collabService.getAllCollaborator(boardId, userId);
                return ResponseEntity.ok(collabDTOS);
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }
    @GetMapping("/{boardId}/collabs/{collabId}")
    public ResponseEntity<CollabDTO> getCollab(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable String collabId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);
            String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

            if (username != null) {
                CollabDTO collabDTO = collabService.getCollaboratorOfBoard(boardId, collabId, userId);
                return ResponseEntity.ok(collabDTO);
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
    public ResponseEntity<CollabDTO> addCollab(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @RequestBody CollabRequestDTO collabRequestDTO) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);
            String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

            Boards boardInfo = boardService.getBoardByBoardId(boardId);
            String boardOwnerName = boardInfo.getUsers().getUsername();
            boolean isCollaborator = collabRepositoriesV3.existsByBoardIdAndUserId(boardId, userId);
            AccessRight accessRight = collabService.getCollabRight(userId);

            if (username.equals(boardOwnerName)) {

                CollabDTO collabDTO = collabService.addCollaborator(userId, boardId, collabRequestDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(collabDTO);
            }

            if (accessRight != null && accessRight == AccessRight.READ) {
                throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Collaborators with READ access cannot add new collaborators");
            }

            if (!isCollaborator) {
                throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Only collaborators can add new collaborators");
            }

            CollabDTO collabDTO = collabService.addCollaborator(userId, boardId, collabRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(collabDTO);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @PatchMapping("/{boardId}/collabs/{collabId}")
    public ResponseEntity<Object> updateCollab(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable String collabId,
            @RequestBody accessRightDTO newAccessRight) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }
        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        if (usernameFromToken.equals(boardOwnerName)) {
            accessRightDTO changeAccessRight = collabService.changeAccessRight(boardId, userId, collabId, newAccessRight);
            return ResponseEntity.ok(changeAccessRight);
        }else {
            throw new AccessRightNotAllow(HttpStatus.FORBIDDEN, "Access Right not allowed");
        }
    }

    @DeleteMapping("/{boardId}/collabs/{collabId}")
    public ResponseEntity<Collab> deleteCollab(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable String collabId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (username != null) {
                Collab collab = collabService.deleteCollaborator(boardId, collabId);
                return ResponseEntity.ok(collab);
            }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }
        String jwt = authHeader.substring(7);
        String usernameFromToken = jwtUtil.extractClaim(jwt, Claims::getSubject);
        String userId = (String) jwtUtil.extractAllClaims(jwt).get("oid");

        Boards boardInfo = boardService.getBoardByBoardId(boardId);
        String boardOwnerName = boardInfo.getUsers().getUsername();

        // Allow owner to delete any collaborator
        if (usernameFromToken.equals(boardOwnerName)) {
            return ResponseEntity.ok(collabService.deleteCollaborator(boardId, collabId));
        }

        // Allow collaborator to delete themselves
        if (userId.equals(collabId)) {
            return ResponseEntity.ok(collabService.deleteCollaborator(boardId, collabId));
        }

        // Deny all other requests
        throw new AccessRightNotAllow(HttpStatus.FORBIDDEN, "Access Right not allowed");
    }

}
