package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.Taskv2;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.StatusIdNotFoundException;
import com.example.integratedbackend.Repositories.StatusRepositories;
import com.example.integratedbackend.Repositories.TasksRepositoriesV2;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<Status> getStatus(){
//        return repositories.findAll();
        return  listMapper.mapList(repositories.findAll(), Status.class,mapper);
    }
    public Status findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Status" + " " + id + " " + "doesn't exist !!!"));
    }

    @Transactional
    public StatusDTO createStatus(NewStatusDTO addStatus) {
            List<Status> statusList= repositories.findAllByNameIgnoreCase(addStatus.getName());
            if (!statusList.isEmpty()){
                throw new StatusIdNotFoundException("must be unique");
            }
        Status status = mapper.map(addStatus, Status.class);
        System.out.println(status.getName());
        System.out.println(status.getDescription());
        Status updatedStatus = repositories.saveAndFlush(status);
        return mapper.map(updatedStatus, StatusDTO.class);
    }

    @Transactional
    public Status deleteStatus(Integer id) throws ItemNotFoundException, BadRequestException {
        Status statusToDelete = repositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("STATUS ID:" + id +  "NOT FOUND"));
        if (statusToDelete.getName().equals("No Status") || statusToDelete.getName().equals("Done")) {
            throw new BadRequestException(statusToDelete.getName() +" cannot be deleted");
        }
        List<Taskv2> tasksUsingStatus = tasksRepositoriesV2.findByStatusId(id);
        if (!tasksUsingStatus.isEmpty()) {
            throw new BadRequestException("Destination status for task transfer not specified.");
        }
        try {
            repositories.delete(statusToDelete);
            return statusToDelete;
        } catch (Exception e) {
            throw new BadRequestException(statusToDelete.getName() +" cannot be deleted");        }
    }

    @Transactional
    public Status transferStatus(Integer oldId, Integer newId) throws BadRequestException {
        if (oldId == newId) {
            throw new ItemErrorNotFoundException("destination status for task transfer must be different from current status.");
        }
        if (!repositories.existsById(oldId)) {
            throw new ItemNotFoundException("Not Found Status Id:" + oldId);
        }
        if (!repositories.existsById(newId)) {
            throw new ItemErrorNotFoundException("the specified status for task transfer does not exist.");
        }
        Status oldStatus = repositories.findById(oldId)
                .orElseThrow(() -> new ItemNotFoundException("StatusEntity not found with id: " + oldId));
        Status newStatus = repositories.findById(newId)
                .orElseThrow(() -> new ItemErrorNotFoundException("the specified status for task transfer does not exist."));
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
    public StatusDTO updateStatus(NewStatusDTO inputStatus, Integer id) {          
        Status status=repositories.findById(id).
                orElseThrow(() -> new ItemNotFoundException("NOT FOUND ID:"+id));
        List<Status> statusList= repositories.findAllByNameIgnoreCase(inputStatus.getName());
        if (!statusList.isEmpty()){ 
            throw new StatusIdNotFoundException("must be unique");
        }
        if (status.getName().equals("No Status") || status.getName().equals("Done")) {
            throw new ItemErrorNotFoundException(status.getName()+" cannot be modified");
        }
        Status updatedStatus = mapper.map(inputStatus, Status.class);
            updatedStatus.setId(id);
            return mapper.map(repositories.save(updatedStatus),StatusDTO.class);
    }

    @Transactional
    public Boolean deleteOrTransfer(Integer id) {
        Status status = repositories.findById(id)
                .orElseThrow(()-> new ItemNotFoundException("Not Found"));
        List<Taskv2> tasks = tasksRepositoriesV2.findAllByStatus(status);
        return !tasks.isEmpty();
    }

}