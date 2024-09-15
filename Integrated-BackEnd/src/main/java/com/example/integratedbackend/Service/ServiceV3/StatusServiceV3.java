package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.DTO.NewStatusDTO;
import com.example.integratedbackend.DTO.StatusDTO;
import com.example.integratedbackend.ErrorHandle.ItemErrorNotFoundException;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.TaskNameDuplicatedException;
import com.example.integratedbackend.Kradankanban.Status;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.BoardsRepositoriesV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.StatusRepositoriesV3;
import com.example.integratedbackend.Service.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceV3 {
    @Autowired
    private StatusRepositoriesV3 statusRepositoriesV3;
    @Autowired
    private BoardsRepositoriesV3 boardsRepositoriesV3;
    @Autowired
    ModelMapper mapper;
    @Autowired
    private ListMapper listMapper;

    public List<StatusV3> getAllStatus(String boardId) {
        return statusRepositoriesV3.findByBoard_BoardId(boardId);
    }

    public StatusV3 findById(String boardId, Integer statusId) throws ItemNotFoundException {
        //อันนี้อะไว้เช็คว่ามี board idที่รับมาจิงมั้ยุถ้าไม่มีโชว์ "Board with ID " + boardId + " not found"
        Boards board = boardsRepositoriesV3.findById(boardId).orElseThrow(() ->
                new ItemNotFoundException("Board with ID " + boardId + " not found"));
        //อันนี้ใช้ repo หาว่า boardId&statusId มันมีตรงกันมั้ยในดาต้าเบส ถ้าไม่ตรงก็โชว์ "Status with ID " + statusId + " doesn't exist on board with ID " + boardId
        return (StatusV3) statusRepositoriesV3.findByBoardBoardIdAndStatusId(boardId, statusId).orElseThrow(() ->
                new ItemNotFoundException("Status with ID " + statusId + " doesn't exist on board with ID " + boardId));
    }


//    @Transactional
//    public StatusV3 createStatus(StatusV3 status, String boardId) {
//        List<StatusV3> statusList = statusRepositoriesV3.findAllByStatusNameIgnoreCase(status.getStatusName());
//        if (!statusList.isEmpty()) {
//            throw new TaskNameDuplicatedException("Status name must be unique");
//        }
//        Boards boards = boardsRepositoriesV3.findByBoardId(boardId)
//                .orElseThrow(() -> new ItemNotFoundException("Board with ID " + boardId + " not found"));
//
//        StatusV3 statusV3 = mapper.map(status, StatusV3.class);
//        statusV3.setBoard(boards);
//
//        return statusRepositoriesV3.saveAndFlush(statusV3);
//    }



//    @Transactional
//    public StatusDTO updateStatus(NewStatusDTO inputStatus, Integer id) {
//        StatusV3 status = statusRepositoriesV3.findById(id).
//                orElseThrow(() -> new ItemNotFoundException("NOT FOUND ID:" + id));
//        List<StatusV3> statusList = StatusRepositoriesV3.findAllByNameIgnoreCase(inputStatus.getName());
//        for (StatusV3 s : statusList) {
//            if (!id.equals(s.getStatusId()) && inputStatus.getName().equalsIgnoreCase(s.getStatusName())) {
//                throw new TaskNameDuplicatedException("must be unique");
//            }
//        }
//        if (status.getStatusName().equalsIgnoreCase("No Status") || status.getStatusName().equalsIgnoreCase("Done")) {
//            throw new ItemErrorNotFoundException("No Status cannot be deleted and Done cannot be deleted respectively");
//        }
//        Status updatedStatus = mapper.map(inputStatus, Status.class);
//        updatedStatus.setId(id);
//        return mapper.map(statusRepositoriesV3.save(updatedStatus), StatusDTO.class);
//    }
}
