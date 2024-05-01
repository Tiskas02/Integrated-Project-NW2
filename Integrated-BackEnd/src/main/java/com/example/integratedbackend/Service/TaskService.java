package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.NewTaskDTO;
import com.example.integratedbackend.Entities.Tasks;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.TasksRepositories;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;


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

    public List<Tasks> getTasks() {
        return repositories.findAll();
    }
    public Tasks findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Task"+ " " + id + " " +"doesn't exist !!!"));
    }
    @Transactional
    public NewTaskDTO createTask(NewTaskDTO addTask) {
        Tasks task = mapper.map(addTask, Tasks.class);
        return mapper.map(repositories.saveAndFlush(task), NewTaskDTO.class);
    }
    @Transactional
    public void deleteTask(Integer id) {
        Tasks delete = repositories.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Task Id " + id + " DOES NOT EXIST !!!"));
        repositories.delete(delete);
        resetAutoIncrement();

    }
    private void resetAutoIncrement() {
        entityManager.createNativeQuery("ALTER TABLE task AUTO_INCREMENT = 1").executeUpdate();
    }
    @Transactional
    public NewTaskDTO updateTask(NewTaskDTO editTask,Integer id){
        Optional<Tasks> optionalTask = repositories.findById(id);
        if (optionalTask.isPresent()) {
            Tasks existingTask = optionalTask.get();
            // Update the existing task entity with data from editTask
            existingTask.setTaskTitle(editTask.getTitle());
            existingTask.setTaskAssignees(editTask.getAssignees());
            existingTask.setTaskDescription(editTask.getDescription());
            existingTask.setTaskStatus(editTask.getStatus());

            // Save the updated task entity back to the database
            Tasks updatedTask = repositories.save(existingTask);

            // Map the updated task entity to NewTaskDTO and return
            return mapper.map(updatedTask, NewTaskDTO.class);
        } else {
            // Handle the case where task with given id is not found
            // You might throw an exception or return null/throw custom exception, etc.
            return null; // Or throw new TaskNotFoundException("Task with id " + id + " not found");
        }
    }


}
