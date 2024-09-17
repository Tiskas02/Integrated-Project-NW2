package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.NewTaskDTO;
import com.example.integratedbackend.DTO.TaskDTO;
import com.example.integratedbackend.DTO.TaskIDDTO;
import com.example.integratedbackend.Kradankanban.Tasks;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Kradankanban.TasksRepositories;
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
public class TaskService {
    @Autowired
    TasksRepositories repositories;
    @Autowired
    ModelMapper mapper;
    @PersistenceContext
    private EntityManager entityManager;
    private final TasksRepositories tasksRepositories;

    public TaskService(TasksRepositories tasksRepositories) {
        this.tasksRepositories = tasksRepositories;
    }

    public List<Tasks> getTasks() {
        return repositories.findAll();
    }

    public Tasks findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        HttpStatus.FORBIDDEN, "Task" + " " + id + " " + "doesn't exist !!!"));
    }

    @Transactional
    public TaskIDDTO createTask(NewTaskDTO addTask) {
        Tasks task = mapper.map(addTask, Tasks.class);
        Tasks updatedTask = repositories.saveAndFlush(task);
        return mapper.map(updatedTask, TaskIDDTO.class);
    }

    @Transactional
    public TaskDTO deleteTask(Integer id) throws ItemNotFoundException {
        Tasks taskToDelete = tasksRepositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("NOT FOUND"));
        tasksRepositories.delete(taskToDelete);
        return mapper.map(taskToDelete, TaskDTO.class);
    }

    @Transactional
    public TaskIDDTO updateTask(NewTaskDTO editTask, Integer id) {
        Optional<Tasks> oldTask = repositories.findById(id);
        if (oldTask.isPresent()) {
            Tasks existingTask = oldTask.get();
            existingTask.setTaskTitle(editTask.getTitle());
            existingTask.setTaskAssignees(editTask.getAssignees());
            existingTask.setTaskDescription(editTask.getDescription());
            existingTask.setTaskStatus(editTask.getStatus());
            Tasks updatedTask = repositories.save(existingTask);
            return mapper.map(updatedTask, TaskIDDTO.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


}