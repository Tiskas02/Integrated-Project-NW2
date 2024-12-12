package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BoardsRepositoriesV3 extends JpaRepository<Boards, String> {
    List<Boards> findBoardsByUsersOid(String oid);
    boolean existsBoardsByNameIgnoreCaseAndUsers(String name, Users user);

    List<Boards> findByIdIn(List<String> boardIds);

    @Modifying
    @Query("DELETE FROM StatusV3 s WHERE s.board.id = :boardId")
    void deleteStatusesByBoardId(@Param("boardId") String boardId);

    @Modifying
    @Query("DELETE FROM Boards b WHERE b.id = :boardId")
    void deleteBoardById(@Param("boardId") String boardId);


}
