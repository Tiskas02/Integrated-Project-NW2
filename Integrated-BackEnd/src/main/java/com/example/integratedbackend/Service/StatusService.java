package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.NewStatusDTO;
import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.Status;
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
    @Autowired
    private ListMapper listMapper;

    public StatusService(StatusRepositories repositories) {
        this.repositories = repositories;
    }

    public List<Status> getStatus() {
        // return repositories.findAll();
        return listMapper.mapList(repositories.findAll(), Status.class, mapper);
    }

    public Status findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Status" + " " + id + " " + "doesn't exist !!!"));
    }

    @Transactional
    public StatusDTO createStatus(NewStatusDTO addStatus) {
        Status status = mapper.map(addStatus, Status.class);
        Status updatedStatus = repositories.saveAndFlush(status);
        return mapper.map(updatedStatus, StatusDTO.class);
    }

    @Transactional
    public Status deleteStatus(Integer id) throws ItemNotFoundException, BadRequestException {
        Status statusToDelete = repositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("STATUS ID:" + id + "NOT FOUND"));
        if (statusToDelete.getName().equals("No Status")) {
            throw new BadRequestException("You can not delete 'No Status'!!");
        }
        if (tasksRepositoriesV2.existsById(statusToDelete.getId())) {
            throw new BadRequestException("Have Some Task On This Status");
        }
        try {
            repositories.delete(statusToDelete);
            return statusToDelete;
        } catch (Exception e) {
            throw new ItemNotFoundException(e.toString());
        }
    }

    @Transactional
    public Status transferStatus(Integer oldId, Integer newId) throws BadRequestException {
        if (oldId == newId) {
            throw new BadRequestException("Old id equal to new Status Id !!!");
        }
        if (!repositories.existsById(oldId)) {
            throw new ItemNotFoundException("Not Found Status Id:" + oldId);
        }
        if (!repositories.existsById(newId)) {
            throw new ItemNotFoundException("Not Found Status Id:" + newId);
        }
        Status oldStatus = repositories.findById(oldId)
                .orElseThrow(() -> new ItemNotFoundException("StatusEntity not found with id: " + oldId));
        Status newStatus = repositories.findById(newId)
                .orElseThrow(() -> new ItemNotFoundException("StatusEntity not found with id: " + newId));
        try {
            oldStatus.setName(newStatus.getName());
            tasksRepositoriesV2.updateTaskStatus(oldStatus, newStatus);
            repositories.delete(oldStatus);
            return oldStatus;
        } catch (Exception e) {
            throw new ItemNotFoundException("Error transferring status: " + e.getMessage());
        }
    }

    @Transactional
    public StatusDTO updateStatus(NewStatusDTO editStatus, Integer id) {
        Status existingStatus = repositories.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Status " + id + " doesn't exist!"));

        existingStatus.setName(editStatus.getName());
        existingStatus.setDescription(editStatus.getDescription());
        
        if (editStatus.getName() != null) {
            Status findStatus = repositories.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + editStatus.getName() + " doesn't exist!"));
            existingStatus.setId(findStatus.getId());
        }
        return mapper.map(existingStatus, StatusDTO.class);
    }
}
