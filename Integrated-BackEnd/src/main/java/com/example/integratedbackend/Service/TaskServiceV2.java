package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.NewTaskDTOV2;
import com.example.integratedbackend.DTO.TaskDTOV2;
import com.example.integratedbackend.DTO.TaskIDDTOV2;
import com.example.integratedbackend.Entities.Taskv2;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Repositories.TasksRepositoriesV2;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TaskServiceV2 {
    @Autowired
    TasksRepositoriesV2 repositories;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
//    @PersistenceContext
//    private EntityManager entityManager;


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

//    public TaskIDDTOV2 createTask(NewTaskDTOV2 addTask) {
//        TasksV2 tasksV2 = ModelMapper.(addTask, TasksV2.class);
//        if (tasksV2.getStatus() != null) {
//            Status findStatus = statusRepositories.findById(tasksV2.getStatus().getStatusId())
//                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + tasksV2.getStatus().getStatusId() + "Not Found"));
//            tasksV2.setStatus(findStatus);
//        }else {
//            Status defaultStatus = statusRepositories.findById(1)
//                    .orElseThrow(() -> new ItemNotFoundException("DEFAULT STATUS IS NOT FOUND "));
//            tasksV2.setStatus(defaultStatus);
//        }
//        return mapper.map(repositories.saveAndFlush(tasksV2), TaskIDDTOV2.class);
//    }
//    @Transactional
//    public TaskDTOV2 deleteTask(Integer id) throws ItemNotFoundException{
//        TasksV2 taskToDelete = tasksRepositories.findById(id)
//                .orElseThrow(() -> new ItemErrorNotFoundException("NOT FOUND"));
//        tasksRepositories.delete(taskToDelete);
//        return mapper.map(taskToDelete, TaskDTOV2.class);
//    }
    @Transactional
    public TaskIDDTOV2 updateTask(NewTaskDTOV2 editTask, Integer id) {
        Taskv2 existingTask = repositories.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task " + id + " doesn't exist!"));

        existingTask.setTaskTitle(editTask.getTitle());
        existingTask.setTaskDescription(editTask.getDescription());
        existingTask.setTaskAssignees(editTask.getAssignees());

        if (editTask.getTitle() != null) {
            Taskv2 findStatus = repositories.findById(editTask.getTaskId())
                    .orElseThrow(() -> new ItemNotFoundException("Status with ID " + editTask.getTaskId() + " doesn't exist!"));
            existingTask.setStatus(findStatus.getStatus());
        }
        return modelMapper.map(existingTask, TaskIDDTOV2.class);
    }
}
