package com.example.integratedbackend.Kradankanban;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepositories extends JpaRepository<Status, Integer> {
    List<Status> findAllByNameIgnoreCase(String name);
}
