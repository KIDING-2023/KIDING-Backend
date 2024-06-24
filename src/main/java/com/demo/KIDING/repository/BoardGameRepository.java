package com.demo.KIDING.repository;

import com.demo.KIDING.domain.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    List<BoardGame> findAll();
    Optional<BoardGame> findById(Long boardGameId);

    boolean existsByName(String name);
    Optional<BoardGame> findByName(String name);

    // JPQL
    @Query("SELECT b FROM BoardGame b WHERE b.name LIKE %:name%")
    Optional<BoardGame> searchByName(@Param("name")String name);

}
