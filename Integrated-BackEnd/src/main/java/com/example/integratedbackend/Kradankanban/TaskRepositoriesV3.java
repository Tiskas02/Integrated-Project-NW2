package com.example.integratedbackend.Kradankanban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface TaskRepositoriesV3 extends JpaRepository<TaskV3, Integer> {
    List<TaskV3> findAllByStatus(Status status);

    @Transactional
    @Modifying
    @Query("UPDATE TaskV3 t SET t.status = :newStatus WHERE t.status = :oldStatus")
    void updateTaskStatus(Status oldStatus, Status newStatus);

    List<TaskV3> findByBoardId(String boardId);

    List<TaskV3> findByBoardIdAndTaskId(Board board, Integer taskId);
}
