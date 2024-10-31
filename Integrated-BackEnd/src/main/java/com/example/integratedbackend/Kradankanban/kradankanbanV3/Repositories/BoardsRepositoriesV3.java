package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardsRepositoriesV3 extends JpaRepository<Boards, String> {
    List<Boards> findBoardsByUsersOid(String oid);
    boolean existsBoardsByNameIgnoreCaseAndUsers(String name, Users user);

    List<Boards> findBoardsById(String boardId);

    List<Boards> findBoardsByCollab(Collab collab);

    List<Boards> findByIdIn(List<String> boardIds);

}
