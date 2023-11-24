package com.demo.KIDING.repository;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.dto.BoardGameRes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    List<BoardGame> findAll();

}
