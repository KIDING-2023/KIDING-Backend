package com.demo.KIDING.repository;

import com.demo.KIDING.domain.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    boolean existsByUserIdAndBoardGameId(Long userId, Long boardGameId);
}
