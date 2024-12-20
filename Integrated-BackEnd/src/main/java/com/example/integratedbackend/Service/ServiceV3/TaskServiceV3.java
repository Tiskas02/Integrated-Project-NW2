package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.NewTaskDTOV3;
import com.example.integratedbackend.DTO.DTOV3.TaskDTOV3;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.StatusIdNotFoundException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.TaskV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.StatusRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.TasksRepositoriesV3;
import com.example.integratedbackend.Service.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                        HttpStatus.NOT_FOUND, "board" + " " + nanoId + " " + "doesn't exist !!!")
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
        List<TaskV3> tasks = tasksRepositoriesV3.findTasksByBoard_Id(boards.getId(), Sort.by(orders));
        return listMapper.mapList(tasks, TaskDTOV3.class, modelMapper);
    }

    public TaskV3 findTaskByBoardIdAndId(Integer id, String boardId) throws ItemNotFoundException {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(() -> new ItemNotFoundException(
                HttpStatus.NOT_FOUND, "Board Id" + " " + boardId + " " + "doesn't exist !!!"));
        return tasksRepositoriesV3.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        HttpStatus.NOT_FOUND, "Task" + " " + id + " " + "doesn't exist !!!"));
    }

    public TaskV3 createTask(NewTaskDTOV3 addTask,String boardId) {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(
                () -> new ItemNotFoundException(
                        HttpStatus.NOT_FOUND, "Board Id" + " " + boardId + " " + "doesn't exist !!!"));
        StatusV3 statusObj = statusRepositoriesV3.findById(addTask.getStatusId()).orElseThrow(
                () -> new StatusIdNotFoundException("does not exist")
        );
        TaskV3 taskV3 = modelMapper.map(addTask, TaskV3.class);
        taskV3.setBoard(boards);
        taskV3.setStatus(statusObj);
        TaskV3 addedTask = tasksRepositoriesV3.saveAndFlush(taskV3);
        return modelMapper.map(addedTask, TaskV3.class);
    }

    @Transactional
    public TaskDTOV3 deleteTask(Integer id, String boardId) throws ItemNotFoundException {
        Boards boards = boardsRepositoriesV3.findById(boardId).orElseThrow(
                () -> new ItemNotFoundException(
                        HttpStatus.FORBIDDEN, "Board Id" + " " + boardId + " " + "doesn't exist !!!"));
        TaskV3 taskToDelete = tasksRepositoriesV3.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.NOT_FOUND, "NOT FOUND"));
        tasksRepositoriesV3.delete(taskToDelete);
        return modelMapper.map(taskToDelete, TaskDTOV3.class);
    }


    @Transactional
    public TaskV3 updateTask(NewTaskDTOV3 editTaskDto, Integer id, String boardId) throws ItemNotFoundException {
        Boards boards = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(
                        HttpStatus.NOT_FOUND, "Board with ID " + boardId + " doesn't exist!"));

        TaskV3 existingTask = tasksRepositoriesV3.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        HttpStatus.NOT_FOUND, "Task with ID " + id + " doesn't exist!"));

        TaskV3 toUpdate = modelMapper.map(editTaskDto, TaskV3.class);

        toUpdate.setId(existingTask.getId());
        toUpdate.setBoard(existingTask.getBoard());

        return tasksRepositoriesV3.save(toUpdate);
    }

    public boolean isTaskAvailable(Integer id, String boardId) {
        return tasksRepositoriesV3.existsByIdAndBoardId(id, boardId);
    }

}
