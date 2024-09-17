package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Kradankanban.Status;
import com.example.integratedbackend.Kradankanban.Taskv2;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.TaskNameDuplicatedException;
import com.example.integratedbackend.Kradankanban.StatusRepositories;
import com.example.integratedbackend.Kradankanban.TasksRepositoriesV2;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private ListMapper listMapper;


    public StatusService(StatusRepositories repositories) {
        this.repositories = repositories;
    }

    public List<Status> getStatus() {
        return listMapper.mapList(repositories.findAll(), Status.class, mapper);
    }

    public Status findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        HttpStatus.FORBIDDEN, "Status" + " " + id + " " + "doesn't exist !!!"));
    }

    @Transactional
    public StatusDTO createStatus(NewStatusDTO addStatus) {
        List<Status> statusList = repositories.findAllByNameIgnoreCase(addStatus.getName());
        if (!statusList.isEmpty()) {
            throw new TaskNameDuplicatedException("must be unique");
        }
        Status status = mapper.map(addStatus, Status.class);
        Status updatedStatus = repositories.saveAndFlush(status);
        return mapper.map(updatedStatus, StatusDTO.class);
    }

    @Transactional
    public Status deleteStatus(Integer id) throws ItemNotFoundException{
        Status statusToDelete = repositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("STATUS ID:" + id + "NOT FOUND"));
        if (statusToDelete.getName().equalsIgnoreCase("No Status") || statusToDelete.getName().equalsIgnoreCase("Done")) {
            throw new ItemErrorNotFoundException(statusToDelete.getName() + " cannot be deleted");
        }
        List<Taskv2> tasksUsingStatus = tasksRepositoriesV2.findByStatusId(id);
        if (!tasksUsingStatus.isEmpty()) {
            throw new ItemErrorNotFoundException("Destination status for task transfer not specified.");
        }
        try {
            repositories.delete(statusToDelete);
            return statusToDelete;
        } catch (Exception e) {
            throw new ItemErrorNotFoundException(statusToDelete.getName() + " cannot be deleted");
        }
    }

    @Transactional
    public Status transferStatus(Integer oldId, Integer newId) throws BadRequestException {
        if (oldId == newId) {
            throw new ItemErrorNotFoundException("destination status for task transfer must be different from current status.");
        }
        if (!repositories.existsById(oldId)) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "Not Found Status Id:" + oldId);
        }
        if (!repositories.existsById(newId)) {
            throw new ItemErrorNotFoundException("the specified status for task transfer does not exist.");
        }
        Status oldStatus = repositories.findById(oldId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "StatusEntity not found with id: " + oldId));
        Status newStatus = repositories.findById(newId)
                .orElseThrow(() -> new ItemErrorNotFoundException("the specified status for task transfer does not exist."));
        try {
            oldStatus.setName(newStatus.getName());
            tasksRepositoriesV2.updateTaskStatus(oldStatus, newStatus);
            repositories.delete(oldStatus);
            return oldStatus;
        } catch (Exception e) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "Error transferring status: " + e.getMessage());
        }
    }

    @Transactional
    public StatusDTO updateStatus(NewStatusDTO inputStatus, Integer id) {
        Status status = repositories.findById(id).
                orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "NOT FOUND ID:" + id));
        List<Status> statusList = repositories.findAllByNameIgnoreCase(inputStatus.getName());
        for (Status s : statusList) {
            if (!id.equals(s.getId()) && inputStatus.getName().equalsIgnoreCase(s.getName())) {
                throw new TaskNameDuplicatedException("must be unique");
            }
        }
        if (status.getName().equalsIgnoreCase("No Status") || status.getName().equalsIgnoreCase("Done")) {
            throw new ItemErrorNotFoundException("No Status cannot be deleted and Done cannot be deleted respectively");
        }
        Status updatedStatus = mapper.map(inputStatus, Status.class);
        updatedStatus.setId(id);
        return mapper.map(repositories.save(updatedStatus), StatusDTO.class);
    }

    @Transactional
    public Boolean deleteOrTransfer(Integer id) {
        Status status = repositories.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "Not Found"));
        List<Taskv2> tasks = tasksRepositoriesV2.findAllByStatus(status);
        return !tasks.isEmpty();
    }

}