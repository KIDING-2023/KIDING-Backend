package com.demo.KIDING.repository;

import com.demo.KIDING.domain.BookMark;
import com.demo.KIDING.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    boolean existsByUserIdAndBoardGameId(Long userId, Long boardGameId);

    Optional<List<BookMark>> findByUserId(Long userId);

    void deleteByUserIdAndBoardGameId(Long userId, Long boardgameId);

    @Transactional
    void deleteByUser(User user);
}
