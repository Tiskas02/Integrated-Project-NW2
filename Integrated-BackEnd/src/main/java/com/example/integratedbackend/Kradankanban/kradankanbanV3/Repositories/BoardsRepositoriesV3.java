package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardsRepositoriesV3 extends JpaRepository<Boards, String> {
//    List<Boards> findAllByUser(Users user);
    List<Boards> findBoardsByUsersOid(String oid);
    boolean existsBoardsByNameIgnoreCaseAndUsers(String name, Users user);
//    Boards existsByNameIgnoreCaseAndOid(String name, String oid);
}
