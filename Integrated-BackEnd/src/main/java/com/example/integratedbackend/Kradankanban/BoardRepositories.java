package com.example.integratedbackend.Kradankanban;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepositories extends JpaRepository<Board, String> {

}
