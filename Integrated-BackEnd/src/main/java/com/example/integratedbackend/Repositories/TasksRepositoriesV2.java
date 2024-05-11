package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.StatusEntity;
import com.example.integratedbackend.Entities.TasksV2;
import com.example.integratedbackend.Entities.Taskv2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

import java.util.List;

public interface TasksRepositoriesV2 extends JpaRepository<Taskv2Entity, Integer> {
    List<Taskv2Entity> findByTaskStatusId(Integer oldStatusId);

    boolean existsByStatus(String statusName);

    void updateTaskStatus(String statusName, String newName);
}
