package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.NewTaskDTO;
import com.example.integratedbackend.DTO.TaskDTO;
import com.example.integratedbackend.DTO.TaskIDDTO;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tasks")
@CrossOrigin(origins = {"http://localhost, http://ip23nw2.sit.kmutt.ac.th,*"})
public class TaskController {
    @Autowired
    TaskService service;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public ResponseEntity<Object> getTasks() {
        return ResponseEntity.ok(listMapper.mapList(service.getTasks(), TaskDTO.class, modelMapper));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findAllProducts(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(service.findByID(id), TaskIDDTO.class));
    }

    @PostMapping("")
    public ResponseEntity<Object> createTask(@RequestBody NewTaskDTO newTask) {
        return ResponseEntity.ok(modelMapper.map(service.createTask(newTask), TaskIDDTO.class));
    }

    @DeleteMapping("{id}")
    public TaskDTO deleteTask(@PathVariable Integer id) {
        return service.deleteTask(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTask(@RequestBody NewTaskDTO editTask,@PathVariable Integer id){
        return ResponseEntity.ok(modelMapper.map(service.updateTask(editTask,id),TaskIDDTO.class));
    }
}