package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.StatusEntity;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.StatusRepositories;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    StatusRepositories repositories;
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
    @Transactional
    public TaskDTOV2 deleteStatus(Integer id) throws ItemNotFoundException{
        StatusEntity statusToDelete = repositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("NOT FOUND"));
        repositories.delete(statusToDelete);
        return mapper.map(statusToDelete, TaskDTOV2.class);
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
