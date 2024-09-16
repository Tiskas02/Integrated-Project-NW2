package com.example.integratedbackend.Controller.ControllerV3;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.DTO.DTOV3.NewBoardDTO;
import com.example.integratedbackend.DTO.DTOV3.NewTaskDTOV3;
import com.example.integratedbackend.DTO.DTOV3.TaskDTOV3;
import com.example.integratedbackend.DTO.DTOV3.TaskV3DTO;
import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.TaskV3;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.ServiceV3.BoardService;
import com.example.integratedbackend.Service.ServiceV3.StatusServiceV3;
import com.example.integratedbackend.Service.ServiceV3.TaskServiceV3;
import com.example.integratedbackend.Service.ServiceV3.UserServiceV3;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/board")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","*"})
public class BoardControllerV3 {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserServiceV3 UserService;
    @Autowired
    private StatusServiceV3 statusServiceV3;
    @Autowired
    private TaskServiceV3 taskServiceV3;
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
                    return ResponseEntity.ok(boards);
                }
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        @PostMapping("")
        public ResponseEntity<Object> createBoard(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody NewBoardDTO newBoard) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);
                if (userId != null) {
                    Boards board = boardService.createBoard(userId, newBoard);
                    return ResponseEntity.ok(board);
                }
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");

        }
    // ================================statuses=====================================
    @GetMapping("{boardId}/statuses")
    public ResponseEntity<Object> getAllStatus(@PathVariable String boardId) {
        return ResponseEntity.ok(listMapper.mapList(statusServiceV3.getAllStatus(boardId), StatusDTO.class, modelMapper));
    }
    @GetMapping("{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> findStatusById( @PathVariable String boardId, @PathVariable Integer statusId) {
        return ResponseEntity.ok(modelMapper.map(statusServiceV3.findById(boardId, statusId), StatusDTO.class));
    }

    @PostMapping("/{boardId}/statuses")
    public ResponseEntity<Object> createStatus(@Valid @RequestBody NewStatusDTO statusV3,@PathVariable String boardId) {
        StatusV3 createdStatus = statusServiceV3.createStatus(statusV3, boardId);
        StatusDTO statusDTO = modelMapper.map(createdStatus, StatusDTO.class);
        return new ResponseEntity<> (statusDTO , HttpStatus.CREATED);
    }
    @PutMapping("/{boardId}/statuses/{statusId}")
    public ResponseEntity<Object> updateStatus(@PathVariable int statusId, @Valid @RequestBody NewStatusDTO status) {
        return new ResponseEntity<>(statusServiceV3.updateStatus(statusId, status) , HttpStatus.OK);
    }
    @DeleteMapping("/statuses/{statusId}")
    public ResponseEntity<Object> deleteStatus(@PathVariable Integer statusId) {
            return new ResponseEntity<>(statusServiceV3.deleteStatus(statusId) , HttpStatus.OK);
    }
    @DeleteMapping("/statuses/{statusId}/{newId}")
    public ResponseEntity<Object> transferStatus(@PathVariable Integer statusId, @PathVariable Integer newId) throws BadRequestException {
        return new ResponseEntity<>(statusServiceV3.transferStatus(statusId, newId), HttpStatus.OK);
    }
    @GetMapping("/statuses/{id}/indicator")
    public ResponseEntity<Boolean> checkDeleteOrTransfer(@PathVariable Integer id) {
        return ResponseEntity.ok(statusServiceV3.deleteOrTransfer(id));
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
            if (userId != null) {
//                List<Boards> boards = boardService.getBoardByUserId(userId);
//                return ResponseEntity.ok(boards);
                List<TaskDTOV3> tasks = taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection, boardId);
                return ResponseEntity.ok(tasks);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
//        return ResponseEntity.ok(taskServiceV3.getAllTasksByBoardId(filterStatuses, sortBy, sortDirection,boardId));
    }
    @GetMapping("{boardId}/task/{id}")
    public ResponseEntity<Object> findTaskByBoardIdAndId(
            @PathVariable Integer id,
            @PathVariable String boardId) {

            TaskV3 task = taskServiceV3.findTaskByBoardIdAndId(id, boardId);
        return ResponseEntity.ok(modelMapper.map(task, TaskIDDTOV2.class));
    }
    @PostMapping("/{boardId}/task")
    public ResponseEntity<Object> createTask(@Valid @RequestBody NewTaskDTOV3 newTask, @PathVariable String boardId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(taskServiceV3.createTask(newTask,boardId), NewTaskReturnV2.class));
    }
    @DeleteMapping("/{boardId}/task/{id}")
    public TaskDTOV3 deleteTask(@PathVariable Integer id) {
        return taskServiceV3.deleteTask(id);
    }
    @PutMapping("/{boardId}/task/{id}")
    public ResponseEntity<Object> updateTask(@Valid @RequestBody NewTaskDTOV3 editTask, @PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(taskServiceV3.updateTask(editTask, id), TaskIDDTOV2.class));
    }
}

