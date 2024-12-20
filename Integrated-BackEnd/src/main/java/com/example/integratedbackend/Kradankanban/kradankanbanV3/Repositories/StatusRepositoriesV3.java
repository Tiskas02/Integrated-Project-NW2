package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepositoriesV3 extends JpaRepository<StatusV3, Integer> {
//    Optional<StatusV3> findByBoardAndId(String boardId, Integer statusId);
    StatusV3 findByStatusNameIgnoreCaseAndBoard(String name, Boards board);

    Optional<Object> findByBoardIdAndStatusId(String boardId, Integer statusId);

    List<StatusV3> findByBoard_Id(String boardId);

//    List<StatusV3> findAllByNameIgnoreCase(String name);
    StatusV3 findByStatusName(String name);

    boolean existsByBoardIdAndStatusId(String boardId, Integer statusId);
}
