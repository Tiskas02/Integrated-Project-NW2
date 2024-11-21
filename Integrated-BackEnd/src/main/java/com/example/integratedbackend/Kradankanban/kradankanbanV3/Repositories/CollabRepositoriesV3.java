package com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.CollabId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollabRepositoriesV3 extends JpaRepository<Collab, CollabId> {
    List<Collab> findByBoardId(String boardId);


    Optional<Collab> findByBoardIdAndUserId(String boardId, String userId);

    Optional<Collab> getCollabByBoardIdAndUserId(String boardId, String userId);

    @Modifying
    @Query("DELETE FROM Collab c WHERE c.boardId = :boardId AND c.userId = :userId")
    void deleteCollabByBoardIdAndUserId(@Param("boardId") String boardId, @Param("userId") String userId);


    Collab getByBoardIdAndUserId(String boardId, String userId);

    List<Collab> findByUserId(String userId);

    List<Collab> findCollabByUserId(String userId);

    boolean existsByBoardIdAndUserId(String boardId, String userId);

    boolean existsCollabByUserId(String userId);

}
