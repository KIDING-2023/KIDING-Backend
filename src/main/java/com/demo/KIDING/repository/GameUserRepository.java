package com.demo.KIDING.repository;

import com.demo.KIDING.domain.GameUser;
import com.demo.KIDING.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameUserRepository extends JpaRepository<GameUser, Long> {

    boolean existsByUserId(Long userId);

    List<GameUser> findByUserId(Long userId);
}
