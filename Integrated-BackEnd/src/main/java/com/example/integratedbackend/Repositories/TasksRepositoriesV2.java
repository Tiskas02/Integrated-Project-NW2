package com.example.integratedbackend.Repositories;


import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.Taskv2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TasksRepositoriesV2 extends JpaRepository<Taskv2, Integer> {
//    List<Taskv2> findByTaskStatusId(Integer oldId);
    @Transactional
    @Modifying
    @Query("UPDATE Taskv2 t SET t.status = :newStatus WHERE t.status = :oldStatus")
    void updateTaskStatus(Status oldStatus, Status newStatus);
}
