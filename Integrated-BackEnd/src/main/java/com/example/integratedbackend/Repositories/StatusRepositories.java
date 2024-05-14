package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepositories extends JpaRepository<Status, Integer> {
    List<Status> findAllByNameIgnoreCase(String name);
}
