package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.DTO.NewTaskReturnV2;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.TaskServiceV2;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/tasks")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
public class TaskControllerV2 {
    @Autowired
    private TaskServiceV2 service;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public ResponseEntity<Object> getAllTask(
            @RequestParam(required = false) List<String> filterStatuses,
            @RequestParam(required = false, defaultValue = "") String[] sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String[] sortDirection
    ) {
        return ResponseEntity.ok(service.getAllTasks(filterStatuses, sortBy, sortDirection));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(service.findByID(id), TaskIDDTOV2.class));
    }

    @PostMapping("")
    public ResponseEntity<Object> createTask(@Valid @RequestBody NewTaskDTOV2 newTask) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(service.createTask(newTask), NewTaskReturnV2.class));
    }

    @DeleteMapping("{id}")
    public TaskDTOV2 deleteTask(@PathVariable Integer id) {
        return service.deleteTask(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTask(@Valid @RequestBody NewTaskDTOV2 editTask, @PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(service.updateTask(editTask, id), TaskIDDTOV2.class));
    }

}