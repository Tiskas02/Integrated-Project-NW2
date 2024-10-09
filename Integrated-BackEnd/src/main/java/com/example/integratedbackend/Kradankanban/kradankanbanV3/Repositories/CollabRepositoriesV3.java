package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.CollabId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollabRepositoriesV3 extends JpaRepository<Collab, CollabId> {
    List<Collab> findByBoardId(String boardId);


    Optional<Collab> findByBoardIdAndUserId(String boardId, String userId);
}
