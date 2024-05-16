package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.NewTaskDTOV2;
import com.example.integratedbackend.DTO.TaskDTOV2;
import com.example.integratedbackend.DTO.TaskIDDTOV2;
import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.Taskv2;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.StatusRepositories;
import com.example.integratedbackend.Repositories.TasksRepositoriesV2;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceV2 {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceV2.class);
    @Autowired
    TasksRepositoriesV2 repositories;
    @Autowired
    StatusRepositories StatusRepositories;
    @Autowired
    StatusRepositories statusRepositories;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

//    @PersistenceContext
//    private EntityManager entityManager;

    public List<TaskDTOV2> getAllTodo(List<String> statusNames, String[] sortBy, String[] direction) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortBy != null && sortBy.length > 0) {
            for (int i = 0; i < sortBy.length; i++) {
                orders.add(new Sort.Order((direction[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy[i]));
            }
        }
        if(statusNames != null){
            List<Status> statuses = new ArrayList<>();
            for (String name:statusNames
            ) {
                List<Status> statusList = statusRepositories.findAllByNameIgnoreCase(name);
                statuses.addAll(statusList);
            }
            return  listMapper.mapList(repositories.findByStatusIn(statuses,Sort.by(orders)), TaskDTOV2.class,modelMapper);
        }
        List<Taskv2> tasks = repositories.findAll(Sort.by(orders));
        return  listMapper.mapList(tasks,TaskDTOV2.class,modelMapper);
    }
    public List<TaskDTOV2> getTasks() {
//        return repositories.findAll();
        return  listMapper.mapList(repositories.findAll(), TaskDTOV2.class,modelMapper);
//
    }
    public Taskv2 findByID(Integer id) throws ItemNotFoundException {
        return repositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Task"+ " " + id + " " +"doesn't exist !!!"));
    }

    public TaskIDDTOV2 createTask(NewTaskDTOV2 addTask) {
        Status statusObj = statusRepositories.findById(addTask.getStatusId()).orElseThrow(
                () -> new ItemNotFoundException("NOT FOUND")
        );
        Taskv2 taskV2 = modelMapper.map(addTask, Taskv2.class);
        taskV2.setStatus(statusObj);
        Taskv2 updatedTask = repositories.saveAndFlush(taskV2);
        return modelMapper.map(updatedTask, TaskIDDTOV2.class);
    }
    @Transactional
    public TaskDTOV2 deleteTask(Integer id) throws ItemNotFoundException{
        Taskv2 taskToDelete = repositories.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("NOT FOUND"));
        repositories.delete(taskToDelete);
        return modelMapper.map(taskToDelete, TaskDTOV2.class);
    }


    @Transactional
    public TaskIDDTOV2 updateTask(NewTaskDTOV2 editTask, Integer id) {
        Taskv2 existingTask = repositories.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task " + id + " doesn't exist!"));
        existingTask.setTaskTitle(editTask.getTitle());
        existingTask.setTaskDescription(editTask.getDescription());
        existingTask.setTaskAssignees(editTask.getAssignees());
        if (editTask.getTitle() != null) {
            Status findStatus = statusRepositories.findById(editTask.getStatusId())
                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + editTask.getId() + " doesn't exist!"));
            existingTask.setStatus(findStatus);
        }
        Taskv2 updatedTask = repositories.save(existingTask);
        return modelMapper.map(updatedTask, TaskIDDTOV2.class);
    }
}