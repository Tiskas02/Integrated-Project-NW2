package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.Tasks;
import com.example.integratedbackend.Entities.TasksV2;
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

    public List<Status> getStatus(){
        return repositories.findAll();
    }

    public Status findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Status"+ " " + id + " " +"doesn't exist !!!"));
    }
    @Transactional
    public TaskIDDTOV2 createStatus(NewTaskDTOV2 addStatus) {
        Status status = mapper.map(addStatus, Status.class);
        return mapper.map(repositories.saveAndFlush(status), TaskIDDTOV2.class);
    }
    @Transactional
    public TaskDTOV2 deleteStatus(Integer id) throws ItemNotFoundException{
        Status statusToDelete = repositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("NOT FOUND"));
        repositories.delete(statusToDelete);
        return mapper.map(statusToDelete, TaskDTOV2.class);
    }
    @Transactional
    public TaskIDDTOV2 updateStatus(NewTaskDTOV2 editStatus, Integer id) {
        Status existingStatus = repositories.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Status " + id + " doesn't exist!"));

        existingStatus.setStatusName(editStatus.getStatus().getStatusName());
        existingStatus.setStatusDescription(editStatus.getDescription());


        if (editStatus.getStatus().getStatusName() != null) {
            Status findStatus = repositories.findById(editStatus.getStatus().getStatusId())
                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + editStatus.getStatus().getStatusName() + " doesn't exist!"));
            existingStatus.setStatusId(findStatus.getStatusId());
        }
        return mapper.map(existingStatus, TaskIDDTOV2.class);
    }
}
