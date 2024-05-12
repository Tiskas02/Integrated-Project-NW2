package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.StatusEntity;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.StatusService;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/statuses")
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
        return ResponseEntity.ok(listMapper.mapList(statusService.getStatus(), StatusDTO.class,modelMapper));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findAllStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(statusService.findByID(id), TaskIDDTOV2.class));
    }

    @PostMapping("")
    public ResponseEntity<Object> createStatus(@RequestBody NewStatusDTO newStatus) {
        return ResponseEntity.ok(modelMapper.map(statusService.createStatus(newStatus), StatusDTO.class));
    }

    @DeleteMapping("{id}")
    public StatusEntity deleteStatus(@PathVariable Integer id) throws BadRequestException {
        return statusService.deleteStatus(id);
    }
    @DeleteMapping("/{id}/{newId}")
    public ResponseEntity<Object> transferStatus(@PathVariable Integer id, @PathVariable Integer newId) throws BadRequestException {
        return new ResponseEntity<>(statusService.transferStatus(id, newId), HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<Object> updateStatus(@RequestBody NewStatusDTO editStatus,@PathVariable Integer id){
        return ResponseEntity.ok(modelMapper.map(statusService.updateStatus(editStatus,id),StatusDTO.class));
    }
}
