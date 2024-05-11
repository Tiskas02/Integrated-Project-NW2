package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepositories extends JpaRepository<Tasks,Integer> {
}
