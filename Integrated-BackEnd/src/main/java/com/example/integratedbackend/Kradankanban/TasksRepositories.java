package com.example.integratedbackend.Kradankanban;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepositories extends JpaRepository<Tasks, Integer> {
}