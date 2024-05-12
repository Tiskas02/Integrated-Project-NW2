package com.example.integratedbackend.Repositories;


import com.example.integratedbackend.Entities.Taskv2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TasksRepositoriesV2 extends JpaRepository<Taskv2Entity, Integer> {
    List<Taskv2Entity> findByTaskStatusId(Integer oldId);

    @Transactional
    @Modifying
    @Query("UPDATE Taskv2Entity t SET t.statusByTaskStatusId = :newStatus WHERE t.statusByTaskStatusId = :oldStatus")
    void updateTaskStatus(String oldStatus, String newStatus);

//    boolean existsByStatus(String status);
}
