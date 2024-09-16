package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.NewTaskDTOV3;
import com.example.integratedbackend.DTO.DTOV3.TaskDTOV3;
import com.example.integratedbackend.DTO.DTOV3.TaskV3DTO;
import com.example.integratedbackend.DTO.NewTaskDTOV2;
import com.example.integratedbackend.DTO.TaskDTOV2;
import com.example.integratedbackend.DTO.TaskIDDTOV2;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.StatusIdNotFoundException;
import com.example.integratedbackend.Kradankanban.Status;
import com.example.integratedbackend.Kradankanban.Taskv2;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.TaskV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.StatusRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.TasksRepositoriesV3;
import com.example.integratedbackend.Service.ListMapper;
import io.viascom.nanoid.NanoId;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceV3 {
    @Autowired
    TasksRepositoriesV3 tasksRepositoriesV3;
    @Autowired
    StatusRepositoriesV3 statusRepositoriesV3;
    @Autowired
    BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    public List<TaskDTOV3> getAllTasksByBoardId(List<String> statusNames, String[] sortBy, String[] direction, String nanoId) {
        Boards boards = boardsRepositoriesV3.findById(nanoId).orElseThrow(
                () -> new ItemNotFoundException(
                        "board" + " " + nanoId + " " + "doesn't exist !!!")
        );
        List<Sort.Order> orders = new ArrayList<>();
        if ((sortBy.length != 0 && !(sortBy[0].equals("status.name"))) || sortBy.length > 1) {
            throw new ItemErrorNotFoundException("invalid filter parameter");
        }
        if (sortBy.length != 0) {
            for (int i = 0; i < sortBy.length; i++) {
                orders.add(new Sort.Order((direction[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy[i]));
            }
        } else {
            orders.add(new Sort.Order(Sort.Direction.ASC, "createdOn"));
        }
        if (statusNames != null) {
            List<StatusV3> statuses = statusNames.stream().map((nameStatus) -> statusRepositoriesV3.findByStatusName(nameStatus.replace("_"," "))).toList();
            return listMapper.mapList(tasksRepositoriesV3.findByStatusInAndBoard(statuses,boards, Sort.by(orders)), TaskDTOV3.class, modelMapper);
        }
        List<TaskV3> tasks = tasksRepositoriesV3.findTasksByBoard_BoardId(boards.getBoardId(), Sort.by(orders));
        return listMapper.mapList(tasks, TaskDTOV3.class, modelMapper);
    }

    public TaskV3 findTaskByBoardIdAndId(Integer id, String boardId) throws ItemNotFoundException {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() -> new ItemNotFoundException(
                "Board Id" + " " + boardId + " " + "doesn't exist !!!"));
        return tasksRepositoriesV3.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Task" + " " + id + " " + "doesn't exist !!!"));
    }

    public TaskDTOV3 createTask(NewTaskDTOV3 addTask,String boardId) {
        StatusV3 statusObj = statusRepositoriesV3.findById(addTask.getStatusId()).orElseThrow(
                () -> new StatusIdNotFoundException("does not exist")
        );
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(
                () -> new ItemNotFoundException(
                        "Board Id" + " " + boardId + " " + "doesn't exist !!!"));
        TaskV3 taskV3 = modelMapper.map(addTask, TaskV3.class);
        taskV3.setBoard(boards);
        taskV3.setStatus(statusObj);
        TaskV3 addedTask = tasksRepositoriesV3.saveAndFlush(taskV3);
        return modelMapper.map(addedTask, TaskDTOV3.class);
    }

    @Transactional
    public TaskDTOV3 deleteTask(Integer id, String boardId) throws ItemNotFoundException {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(
                () -> new ItemNotFoundException(
                        "Board Id" + " " + boardId + " " + "doesn't exist !!!"));
        TaskV3 taskToDelete = tasksRepositoriesV3.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("NOT FOUND"));
        tasksRepositoriesV3.delete(taskToDelete);
        return modelMapper.map(taskToDelete, TaskDTOV3.class);
    }


    @Transactional
    public TaskV3 updateTask(NewTaskDTOV3 editTask, Integer id, String boardId) throws ItemNotFoundException {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(
                () -> new ItemNotFoundException(
                        "Board Id" + " " + boardId + " " + "doesn't exist !!!"));
        TaskV3 existingTask = tasksRepositoriesV3.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task " + id + " doesn't exist!"));
        existingTask.setTitle(editTask.getTitle());
        existingTask.setDescription(editTask.getDescription());
        existingTask.setAssignees(editTask.getAssignees());
        if (editTask.getTitle() != null) {
            StatusV3 findStatus = statusRepositoriesV3.findById(editTask.getStatusId())
                    .orElseThrow(() -> new StatusIdNotFoundException("does not exist"));
            existingTask.setStatus(findStatus);
        }
        TaskV3 updatedTask = tasksRepositoriesV3.save(existingTask);
        return modelMapper.map(updatedTask, TaskV3.class);
    }
}
