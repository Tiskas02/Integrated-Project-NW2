package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Kradankanban.Status;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.StatusService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/statuses")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","*"})
public class StatusController {
    @Autowired
    StatusService statusService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public ResponseEntity<Object> getStatus() {
        return ResponseEntity.ok(listMapper.mapList(statusService.getStatus(), StatusDTO.class, modelMapper));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findStatusById(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(statusService.findByID(id), StatusDTO.class));
    }

    @PostMapping("")
    public ResponseEntity<Object> createStatus(@Valid @RequestBody NewStatusDTO newStatus) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(statusService.createStatus(newStatus), StatusDTO.class));
    }

    @DeleteMapping("{id}")
    public Status deleteStatus(@PathVariable Integer id) throws BadRequestException {
        return statusService.deleteStatus(id);
    }

    @DeleteMapping("/{id}/{newId}")
    public ResponseEntity<Object> transferStatus(@PathVariable Integer id, @PathVariable Integer newId) throws BadRequestException {
        return new ResponseEntity<>(statusService.transferStatus(id, newId), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateStatus(@Valid @RequestBody NewStatusDTO editStatus, @PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(statusService.updateStatus(editStatus, id), StatusDTO.class));
    }

    @GetMapping("{id}/indicator")
    public ResponseEntity<Boolean> checkDeleteOrTransfer(@PathVariable Integer id) {
        return ResponseEntity.ok(statusService.deleteOrTransfer(id));
    }
}