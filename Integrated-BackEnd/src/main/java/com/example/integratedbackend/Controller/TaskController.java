package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.TaskDTO;
import com.example.integratedbackend.DTO.TaskIDDTO;
import com.example.integratedbackend.Entities.Tasks;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {
    @Autowired
    TaskService service;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @GetMapping("")
    public ResponseEntity<Object> getTasks(){
         return ResponseEntity.ok(listMapper.mapList(service.getTasks(), TaskDTO.class,modelMapper));
   }
    @GetMapping("{id}")
    public ResponseEntity<Object> findAllProducts(@PathVariable Integer id){
        return ResponseEntity.ok(modelMapper.map(service.findByID(id), TaskIDDTO.class));
    }
}
