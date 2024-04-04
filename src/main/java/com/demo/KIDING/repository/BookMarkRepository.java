package com.demo.KIDING.repository;

import com.demo.KIDING.domain.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    boolean existsByUserIdAndBoardGameId(Long userId, Long boardGameId);

    Optional<List<BookMark>> findByUserId(Long userId);
}
