package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.StatusEntity;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.StatusRepositories;
import com.example.integratedbackend.Repositories.TasksRepositoriesV2;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    StatusRepositories repositories;
    @Autowired
    TasksRepositoriesV2 tasksRepositoriesV2;
    @Autowired
    ModelMapper mapper;

    public StatusService(StatusRepositories repositories) {
        this.repositories = repositories;
    }
    public List<StatusEntity> getStatus(){
        return repositories.findAll();
    }
    public StatusEntity findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Status"+ " " + id + " " +"doesn't exist !!!"));
    }
    @Transactional
    public TaskIDDTOV2 createStatus(NewTaskDTOV2 addStatus) {
        StatusEntity status = mapper.map(addStatus, StatusEntity.class);
        return mapper.map(repositories.saveAndFlush(status), TaskIDDTOV2.class);
    }
    //delete and check tranfer status
//    Old version delete status
    @Transactional
    public StatusEntity deleteStatus(Integer id) throws ItemNotFoundException, BadRequestException {
        StatusEntity statusToDelete = repositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("STATUS ID:" + id +  "NOT FOUND"));
        if (tasksRepositoriesV2.existsByStatus(statusToDelete.getStatusName())) {
            throw new BadRequestException("Have Some Task On This Status");
        }
        if (statusToDelete.getStatusName().equals("No Status")) {
            throw new BadRequestException("You can not delete 'No Status'!!");
        }
        try {
            repositories.delete(statusToDelete);
            return statusToDelete;
        }catch (Exception e) {
            throw new ItemNotFoundException(e.toString());
        }

    }
    @Transactional
    public StatusEntity transferStatus(Integer oldId, Integer newId) throws BadRequestException {
        if (oldId == newId) {
            throw new BadRequestException("Old id equal to new Status Id !!!");
        }
        if (!repositories.existsById(oldId)) {
            throw new ItemNotFoundException("Not Found Status Id:" + oldId);
        }
        if (!repositories.existsById(newId)) {
            throw new ItemNotFoundException("Not Found Status Id:" + newId);
        }
        StatusEntity oldstatus = repositories.findById(oldId)
                .orElseThrow();
        String newName = repositories.findById(newId).orElseThrow().getStatusName();
        try {
            tasksRepositoriesV2.updateTaskStatus(oldstatus.getStatusName(), newName);
            repositories.delete(oldstatus);
            return oldstatus;
        }catch (Exception msg) {
            throw new ItemNotFoundException(msg.toString());
        }

    }
    @Transactional
    public TaskIDDTOV2 updateStatus(NewTaskDTOV2 editStatus, Integer id) {
        StatusEntity existingStatus = repositories.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Status " + id + " doesn't exist!"));

        existingStatus.setStatusName(editStatus.getStatusName());
        existingStatus.setStatusDescription(editStatus.getDescription());

        if (editStatus.getStatusName() != null) {
            StatusEntity findStatus = repositories.findById(editStatus.getTaskId())
                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + editStatus.getStatusName() + " doesn't exist!"));
            existingStatus.setStatusId(findStatus.getStatusId());
        }
        return mapper.map(existingStatus, TaskIDDTOV2.class);
    }
}
