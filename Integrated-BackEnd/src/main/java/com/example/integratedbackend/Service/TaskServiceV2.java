package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.TasksV2;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.TasksRepositoriesV2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceV2 {
    @Autowired
    TasksRepositoriesV2 repositories;
    @Autowired
    ModelMapper mapper;
    @PersistenceContext
    private EntityManager entityManager;
    private final TasksRepositoriesV2 tasksRepositories;

    public TaskServiceV2(TasksRepositoriesV2 tasksRepositories) {
        this.tasksRepositories = tasksRepositories;
    }

    public List<TasksV2> getTasks() {
        return repositories.findAll();
    }
    public TasksV2 findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Task"+ " " + id + " " +"doesn't exist !!!"));
    }
    @Transactional
    public TaskIDDTOV2 createTask(NewTaskDTOV2 addTask) {
        TasksV2 task = mapper.map(addTask, TasksV2.class);
        TasksV2 updatedTask = repositories.saveAndFlush(task);
        return mapper.map(updatedTask, TaskIDDTOV2.class);
    }
    @Transactional
    public TaskDTOV2 deleteTask(Integer id) throws ItemNotFoundException{
        TasksV2 taskToDelete = tasksRepositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("NOT FOUND"));
        tasksRepositories.delete(taskToDelete);
        return mapper.map(taskToDelete, TaskDTOV2.class);
    }
    @Transactional
    public TaskIDDTOV2 updateTask(NewTaskDTOV2 editTask, Integer id) {
        Optional<TasksV2> oldTask = repositories.findById(id);
        if (oldTask.isPresent()) {
            TasksV2 existingTask = oldTask.get();
            existingTask.setTaskTitle(editTask.getTitle());
            existingTask.setTaskAssignees(editTask.getAssignees());
            existingTask.setTaskDescription(editTask.getDescription());
            existingTask.setTaskStatus(editTask.getStatus());
            TasksV2 updatedTask = repositories.save(existingTask);
            return mapper.map(updatedTask, TaskIDDTOV2.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }



}
