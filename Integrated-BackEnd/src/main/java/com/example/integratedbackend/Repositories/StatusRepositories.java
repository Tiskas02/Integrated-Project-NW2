package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepositories extends JpaRepository<Status, Integer> {

//    boolean existsByStatusName(String status);
}
