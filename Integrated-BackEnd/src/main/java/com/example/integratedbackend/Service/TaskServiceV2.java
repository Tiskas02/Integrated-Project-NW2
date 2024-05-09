package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.*;
import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.TasksV2;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.StatusRepositories;
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
    private final StatusRepositories statusRepositories;

    public TaskServiceV2(TasksRepositoriesV2 tasksRepositories,
                         StatusRepositories statusRepositories) {
        this.tasksRepositories = tasksRepositories;
        this.statusRepositories = statusRepositories;
    }

    public List<TasksV2> getTasks() {
        return repositories.findAll();
    }
    public TasksV2 findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Task"+ " " + id + " " +"doesn't exist !!!"));
    }
//    edit for version 2
//    @Transactional
//    public TaskIDDTOV2 createTask(NewTaskDTOV2 addTask) {
//        TasksV2 task = mapper.map(addTask, TasksV2.class);
//        TasksV2 updatedTask = repositories.saveAndFlush(task);
//        return mapper.map(updatedTask, TaskIDDTOV2.class);
//    }
@Transactional
public TaskIDDTOV2 createTask(NewTaskDTOV2 addTask) {
        TasksV2 tasksV2 = mapper.map(addTask, TasksV2.class);
        if (tasksV2.getStatus() != null) {
            Status findStatus = statusRepositories.findById(tasksV2.getStatus().getTaskId())
                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + tasksV2.getStatus().getTaskId() + "Not Found"));
            tasksV2.setStatus(findStatus);
        }else {
            Status defaultStatus = statusRepositories.findById(1)
                    .orElseThrow(() -> new ItemNotFoundException("DEFAULT STATUS IS NOT FOUND "));
            tasksV2.setStatus(defaultStatus);
        }
        return mapper.map(repositories.saveAndFlush(tasksV2), TaskIDDTOV2.class);
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
        TasksV2 existingTask = repositories.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task " + id + " doesn't exist!"));

        existingTask.setTaskTitle(editTask.getTitle());
        existingTask.setTaskDescription(editTask.getDescription());
        existingTask.setTaskAssignees(editTask.getAssignees());

        if (editTask.getStatus() != null) {
            TasksV2 findStatus = repositories.findById(editTask.getStatus().getTaskId())
                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + editTask.getStatus().getTaskId() + " doesn't exist!"));
            existingTask.setStatus(findStatus.getStatus());
        }
        return mapper.map(existingTask, TaskIDDTOV2.class);
    }
}
