package com.example.integratedbackend.Service;

import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Kradankanban.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceV3 {
    @Autowired
    private TaskRepositoriesV3 taskRepositoriesV3;
    @Autowired
    private StatusRepositories statusRepositories;
    @Autowired
    private BoardRepositories boardRepositories;
    @Autowired
    ModelMapper modelMapper;


    public List<TaskV3> findAllTasksByBoardId(String boardId, String userId) {
        Board board = boardRepositories.findById(boardId).orElseThrow(()-> new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board not found"));
        if (!board.getUserId().equals(userId)) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "You are not the owner of this board");
        }
        return taskRepositoriesV3.findByBoardId(boardId);
    }

    public TaskV3 findTaskByBoardIdAndTaskId(String boardId, String userId, Integer taskId) {
        Board board = boardRepositories.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board not found"));
        if (!board.getUserId().equals(userId)) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "You are not the owner of this board");
        }

        TaskV3 taskV3 = (TaskV3) taskRepositoriesV3.findByBoardIdAndTaskId(board, taskId);
        if (taskV3 == null) {
            throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Task ID"+ taskId + " not found");
        }
        return taskV3;
    }

}
