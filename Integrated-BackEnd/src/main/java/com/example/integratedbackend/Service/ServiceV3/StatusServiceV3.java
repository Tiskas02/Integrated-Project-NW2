package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.DTOV3.NewStatusIDDTO;
import com.example.integratedbackend.DTO.NewStatusDTO;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.TaskNameDuplicatedException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.TaskV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.StatusRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.TasksRepositoriesV3;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceV3 {
    @Autowired
    private StatusRepositoriesV3 statusRepositoriesV3;
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    private TasksRepositoriesV3 tasksRepositoriesV3;
    @Autowired
    ModelMapper mapper;

    public List<StatusV3> getAllStatus(String boardId) {
        return statusRepositoriesV3.findByBoard_BoardId(boardId);
    }

    public StatusV3 findById(String boardId, Integer statusId) throws ItemNotFoundException {
        //อันนี้อะไว้เช็คว่ามี board idที่รับมาจิงมั้ยุถ้าไม่มีโชว์ "Board with ID " + boardId + " not found"
        Boards board = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board with ID " + boardId + " not found"));
        //อันนี้ใช้ repo หาว่า boardId&statusId มันมีตรงกันมั้ยในดาต้าเบส ถ้าไม่ตรงก็โชว์ "Status with ID " + statusId + " doesn't exist on board with ID " + boardId
        return (StatusV3) statusRepositoriesV3.findByBoardBoardIdAndStatusId(boardId, statusId).orElseThrow(() ->
                new ItemNotFoundException(HttpStatus.FORBIDDEN, "Status with ID " + statusId + " doesn't exist on board with ID " + boardId));
    }


    @Transactional
    public StatusV3 createStatus(NewStatusDTO status, String boardId) {
        Boards boards = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board with ID " + boardId + " not found"));
        StatusV3 oldStatus = statusRepositoriesV3.findByStatusNameIgnoreCaseAndBoard(status.getName(),boards);
        if (oldStatus != null){
            throw new TaskNameDuplicatedException("must be unique");
        }
        StatusV3 statusV3 = new StatusV3();
        statusV3.setStatusName(status.getName());
        statusV3.setStatusDescription(status.getDescription());
        statusV3.setBoard(boards);
        return statusRepositoriesV3.saveAndFlush(statusV3);
    }
    @Transactional
    public void createDefaultStatus(String boardId) {
        saveNewStatus("No Status", "The default status", boardId);
        saveNewStatus("Done", "", boardId);
        saveNewStatus("To Do", "", boardId);
        saveNewStatus("Doing", "", boardId);
    }

    private void saveNewStatus(String name, String description, String boardId) {
        NewStatusDTO newStatusDTO = new NewStatusDTO();
        newStatusDTO.setName(name);
        newStatusDTO.setDescription(description);
        StatusV3 status = mapper.map(newStatusDTO, StatusV3.class);
        Boards boards = boardsRepositoriesV3.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board with ID " + boardId + " not found"));
        status.setBoard(boards);
        statusRepositoriesV3.save(status);
    }


    @Transactional
    public NewStatusIDDTO updateStatus(Integer statusId, NewStatusDTO status) {
        StatusV3 oldStatusV3 = statusRepositoriesV3.findById(statusId).
                orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "NOT FOUND ID:" + statusId));
        StatusV3 statusDuplicate = statusRepositoriesV3.findByStatusNameIgnoreCaseAndBoard(status.getName(),oldStatusV3.getBoard());
        if (statusDuplicate != null && !oldStatusV3.getStatusName().equalsIgnoreCase(status.getName())){
                throw new TaskNameDuplicatedException("must be unique");
        }
        if (status.getName().equalsIgnoreCase("No Status") || status.getName().equalsIgnoreCase("Done")) {
            throw new ItemErrorNotFoundException("No Status cannot be deleted and Done cannot be deleted respectively");
        }
        StatusV3 updatedStatus = mapper.map(status, StatusV3.class);
        updatedStatus.setStatusId(statusId);
        updatedStatus.setBoard(oldStatusV3.getBoard());
        return mapper.map(statusRepositoriesV3.save(updatedStatus), NewStatusIDDTO.class);
    }
    @Transactional
    public StatusV3 deleteStatus(Integer id) throws ItemNotFoundException{
        StatusV3 statusToDelete = statusRepositoriesV3.findById(id)
                .orElseThrow(() -> new ItemErrorNotFoundException("STATUS ID:" + id + "NOT FOUND"));
        if (statusToDelete.getStatusName().equalsIgnoreCase("No Status") || statusToDelete.getStatusName().equalsIgnoreCase("Done")) {
            throw new ItemErrorNotFoundException(statusToDelete.getStatusName() + " cannot be deleted");
        }
        if (statusToDelete.getTasks().size() != 0) {
            throw new ItemErrorNotFoundException("Destination status for task transfer not specified.");
        }
        try {
            statusRepositoriesV3.delete(statusToDelete);
            return statusToDelete;
        } catch (Exception e) {
            throw new ItemErrorNotFoundException(statusToDelete.getStatusName() + " cannot be deleted");
        }
    }
    @Transactional
    public int transferStatus(Integer oldId, Integer newId) throws BadRequestException {
        if (oldId == newId) {
            throw new ItemErrorNotFoundException("destination status for task transfer must be different from current status.");
        }
        if (!statusRepositoriesV3.existsById(oldId)) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "Not Found Status Id:" + oldId);
        }
        if (!statusRepositoriesV3.existsById(newId)) {
            throw new ItemErrorNotFoundException("the specified status for task transfer does not exist.");
        }
        StatusV3 oldStatus = statusRepositoriesV3.findById(oldId)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "StatusEntity not found with id: " + oldId));
        StatusV3 newStatus = statusRepositoriesV3.findById(newId)
                .orElseThrow(() -> new ItemErrorNotFoundException("the specified status for task transfer does not exist."));
        try {
            oldStatus.setStatusName(newStatus.getStatusName());
            int taskCount = oldStatus.getTasks().size();
            tasksRepositoriesV3.updateTaskStatus(oldStatus, newStatus);
            statusRepositoriesV3.delete(oldStatus);
            return taskCount;
        } catch (Exception e) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "Error transferring status: " + e.getMessage());
        }
    }
    @Transactional
    public Boolean deleteOrTransfer(Integer id) {
        StatusV3 status = statusRepositoriesV3.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "Not Found"));
        List<TaskV3> tasks = tasksRepositoriesV3.findAllByStatus(status);
        return !tasks.isEmpty();
    }
}
