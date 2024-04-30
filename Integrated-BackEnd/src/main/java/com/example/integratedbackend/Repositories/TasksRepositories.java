package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasksRepositories extends JpaRepository<Tasks,Integer> {
}
