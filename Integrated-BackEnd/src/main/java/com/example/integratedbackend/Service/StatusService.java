package com.example.integratedbackend.Service;

import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.StatusRepositories;
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
                        "Task"+ " " + id + " " +"doesn't exist !!!"));
    }
}
