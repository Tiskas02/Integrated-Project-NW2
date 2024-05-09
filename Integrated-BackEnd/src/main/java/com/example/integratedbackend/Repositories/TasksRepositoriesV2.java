package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.TasksV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepositoriesV2 extends JpaRepository<TasksV2,Integer> {
}
