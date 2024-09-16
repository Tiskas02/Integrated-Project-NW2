package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.TaskV3;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TasksRepositoriesV3 extends JpaRepository<TaskV3,Integer> {
    List<TaskV3> findAllByStatus(StatusV3 status);
    List<TaskV3> findAllByBoard(String boards, Sort sort);
    List<TaskV3> findByStatusInAndAndBoard(List<StatusV3> statusV3,Boards board, Sort sort);

    @Transactional
    @Modifying
    @Query("UPDATE TaskV3 t SET t.status = :newStatus WHERE t.status = :oldStatus")
    void updateTaskStatus(StatusV3 oldStatus, StatusV3 newStatus);
}
