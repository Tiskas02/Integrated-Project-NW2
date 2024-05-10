package com.example.integratedbackend.Repositories;

import com.example.integratedbackend.Entities.Status;
import com.example.integratedbackend.Entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepositories extends JpaRepository<StatusEntity, Integer> {
}
