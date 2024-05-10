package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.StatusEntity;
import com.example.integratedbackend.Entities.TasksV2;
import com.example.integratedbackend.Entities.Taskv2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

public interface TasksRepositoriesV2 extends JpaRepository<Taskv2Entity, Integer> {
}
