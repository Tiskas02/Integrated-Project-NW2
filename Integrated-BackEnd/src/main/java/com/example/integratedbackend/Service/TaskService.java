package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.NewTaskDTO;
import com.example.integratedbackend.Entities.Tasks;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.TasksRepositories;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;

@Service
public class TaskService {
    @Autowired
    TasksRepositories repositories;
    @Autowired
    ModelMapper mapper;

    public List<Tasks> getTasks() {
        return repositories.findAll();
    }
    public Tasks findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Task"+ " " + id + " " +"doesn't exist !!!"));
    }
    public NewTaskDTO createTask(NewTaskDTO addTask) {
        Tasks task = mapper.map(addTask, Tasks.class);
        return mapper.map(repositories.saveAndFlush(task), NewTaskDTO.class);
    }
    @Transactional
    public void deleteTask(Integer id) {
        Tasks delete = repositories.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Task Id " + id + " DOES NOT EXIST !!!"));
        repositories.delete(delete);
    }


}
