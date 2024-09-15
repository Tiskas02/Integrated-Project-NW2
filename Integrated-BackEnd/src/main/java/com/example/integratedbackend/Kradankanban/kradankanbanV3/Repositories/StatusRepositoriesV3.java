package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.StatusV3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepositoriesV3 extends JpaRepository<StatusV3, Integer> {
//    Optional<StatusV3> findByBoardAndId(String boardId, Integer statusId);
    List<StatusV3> findAllByStatusNameIgnoreCase(String name);

    Optional<Object> findByBoardBoardIdAndStatusId(String boardId, Integer statusId);

    List<StatusV3> findByBoard_BoardId(String boardId);


}
