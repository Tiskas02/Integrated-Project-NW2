package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.NewTaskDTOV2;
import com.example.integratedbackend.DTO.TaskDTOV2;
import com.example.integratedbackend.DTO.TaskIDDTOV2;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.StatusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/status")
@CrossOrigin(origins = {"http://localhost, http://ip23nw2.sit.kmutt.ac.th ,http://intproj23.sit.kmutt.ac.th,*"})
public class StatusController {
    @Autowired
    StatusService statusService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public ResponseEntity<Object> getStatus() {
        return ResponseEntity.ok(listMapper.mapList(statusService.getStatus(), TaskDTOV2.class, modelMapper));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findAllStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(statusService.findByID(id), TaskIDDTOV2.class));
    }

    @PostMapping("")
    public ResponseEntity<Object> createStatus(@RequestBody NewTaskDTOV2 newStatus) {
        return ResponseEntity.ok(modelMapper.map(statusService.createStatus(newStatus), TaskIDDTOV2.class));
    }

    @DeleteMapping("{id}")
    public TaskDTOV2 deleteStatus(@PathVariable Integer id) {
        return statusService.deleteStatus(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateStatus(@RequestBody NewTaskDTOV2 editStatus,@PathVariable Integer id){
        return ResponseEntity.ok(modelMapper.map(statusService.updateStatus(editStatus,id),TaskIDDTOV2.class));
    }
}
