package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.Taskv2;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.TaskServiceV2;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/tasks")
@CrossOrigin(origins = {"http://localhost, http://ip23nw2.sit.kmutt.ac.th,*"})
public class TaskControllerV2 {
        @Autowired
        TaskServiceV2 service;
        @Autowired
        private ModelMapper modelMapper;
        @Autowired
        private ListMapper listMapper;

    @GetMapping("")
    public ResponseEntity<Object> getTasks() {
        return ResponseEntity.ok(listMapper.mapList(service.getTasks(), TaskDTOV2.class, modelMapper));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(service.findByID(id), TaskIDDTOV2.class));
    }
        @PostMapping("")
        public ResponseEntity<Object> createTask(@RequestBody NewTaskDTOV2 newTask) {
            return ResponseEntity.ok(modelMapper.map(service.createTask(newTask), TaskIDDTOV2.class));
        }

        @DeleteMapping("{id}")
        public TaskDTOV2 deleteTask(@PathVariable Integer id) {
            return service.deleteTask(id);
        }
        @GetMapping("status/{id}/indicator")
        public ResponseEntity<Boolean> checkDeleteOrTransfer(@PathVariable Integer id) {
        return ResponseEntity.ok(service.deleteOrTransfer(id));
        }


        @PutMapping("{id}")
        public ResponseEntity<Object> updateTask(@RequestBody NewTaskDTOV2 editTask,@PathVariable Integer id){
            return ResponseEntity.ok(modelMapper.map(service.updateTask(editTask,id),TaskIDDTOV2.class));
        }

    }
