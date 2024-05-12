package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.StatusEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StatusRepositories extends JpaRepository<StatusEntity, Integer> {

    boolean existsByStatusName(String status);
}
