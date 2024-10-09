package com.example.integratedbackend.Controller.ControllerV3;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.DTO.DTOV3.*;
import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.*;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.ServiceV3.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    // ================================Board=====================================
    @GetMapping("")
    public ResponseEntity<Object> getBoardByUserId(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);
            if (userId != null) {
                List<Boards> boards = boardService.getBoardByUserId(userId);
                return new ResponseEntity<>(boards, HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Object> getBoardByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                Boards boards = boardService.getBoardByBoardId(boardId);
                return new ResponseEntity<>(boards, HttpStatus.OK);
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
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
        String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
        if (newBoard == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Board name cannot be empty");
        }
        if (newBoard.getName() == null || newBoard.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Board creation failed. Name is required.");
        }
        Boards board = boardService.createBoard(userId, newBoard);
        if (board.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Board creation failed. Please check the input data.");
        }
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    // ================================statuses=====================================
    @GetMapping("{boardId}/statuses")
    public ResponseEntity<Object> getAllStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                if (boardService.getBoardByBoardId(boardId) != null) {
                    return ResponseEntity.ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId), StatusDTO.class, modelMapper));
                }
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> findStatusById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String boardId,
            @PathVariable Integer statusId) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                return ResponseEntity.ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null){
                return new ResponseEntity<>(statusDTO, HttpStatus.CREATED);
            }else {
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                boardService.getBoardByBoardId(boardId);
                return new ResponseEntity<>(modelMapper.map(statusServiceV3.updateStatus(statusId, status), StatusDtoV3.class), HttpStatus.OK);
            }else{
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                return new ResponseEntity<>(statusServiceV3.deleteStatus(statusId), HttpStatus.OK);
            }else {
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                return new ResponseEntity<>(statusServiceV3.transferStatus(statusId, newId), HttpStatus.OK);
            }else {
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                return ResponseEntity.ok(statusServiceV3.deleteOrTransfer(statusId));
            }else {
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
            @RequestParam(required = false, defaultValue = "ASC") String[] sortDirection
    ) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                if (userId != null) {
                    return ResponseEntity.ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId));
                } else if (userId == null) {
                    return new ResponseEntity<>("Authentication Problem", HttpStatus.NOT_FOUND);
                }
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    @GetMapping("{boardId}/tasks/{id}")
    public ResponseEntity<Object> findTaskByBoardId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @PathVariable String boardId) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);

            if (visibleValue && userId != null) {
                TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
                return ResponseEntity.ok(modelMapper.map(task, TaskDTOV3.class));
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "THIS BOARD IS PRIVATE!!");
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(taskServiceV3.createTask(newTask, boardId), NewTaskReturnV3.class));
            }else {
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                return taskServiceV3.deleteTask(id, boardId);
            }else {
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            boolean visibleValue = visibilityService.checkVisibility(boardId);
            if (visibleValue && userId != null) {
                TaskV3 editedTask = taskServiceV3.updateTask(editTask, id, boardId);
                return ResponseEntity.ok(editedTask);
            }else {
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
            String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);

            if (userId != null) {
                Visibilities updatedVisibility = visibilityService.changeVisibility(boardId, newVisibility);
                return ResponseEntity.ok(updatedVisibility);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
    }

    // ================================Collaborator=====================================

    @GetMapping("/{boardId}/collabs")
    public ResponseEntity<List<Collab>> getAllCollabs(@PathVariable String boardId, HttpServletRequest request) {
        List<Collab> collabs = collabService.getAllCollaborator(boardId);
        return ResponseEntity.ok(collabs);
    }

    @GetMapping("/{boardId}/collab/{callabId}")
    public ResponseEntity<Collab> getCollab(@PathVariable String boardId, @PathVariable String callabId) {
        Collab collab = collabService.getCollaborator(boardId, callabId);
        return ResponseEntity.ok(collab);
    }

    @PostMapping("/{boardId}/collabs")
    public ResponseEntity<Collab> addCollab(@PathVariable String boardId, @RequestBody CollabRequestDTO collabRequestDTO) {
        Collab collab = collabService.addCollaborator(boardId, collabRequestDTO);
        return new ResponseEntity<>(collab, HttpStatus.CREATED);
    }
}

