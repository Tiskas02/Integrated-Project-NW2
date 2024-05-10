package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.NewTaskDTOV2;
import com.example.integratedbackend.DTO.TaskDTOV2;
import com.example.integratedbackend.DTO.TaskIDDTOV2;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.StatusService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

//    @DeleteMapping("{id}")
//    public TaskDTOV2 deleteStatus(@PathVariable Integer id) {
//        return statusService.deleteStatus(id);
//    }
    @DeleteMapping("{id}")
    public ResponseEntity<Object> tranferAndDeeleteStatus(@PathVariable Integer id, @PathVariable Integer newStatusId) {
        try {
            statusService.transferAndDeleteStatus(id, newStatusId);
            return ResponseEntity.ok("The task(s) have been transferred and the status has been deleted.");
        } catch (ItemNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An error has occurred, the status does not exist.");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while transferring tasks and deleting status.");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateStatus(@RequestBody NewTaskDTOV2 editStatus,@PathVariable Integer id){
        return ResponseEntity.ok(modelMapper.map(statusService.updateStatus(editStatus,id),TaskIDDTOV2.class));
    }
}
