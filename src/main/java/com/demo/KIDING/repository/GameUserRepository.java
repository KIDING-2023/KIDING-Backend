package com.demo.KIDING.repository;

import com.demo.KIDING.domain.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameUserRepository extends JpaRepository<GameUser, Long> {

    boolean existsByUserId(Long userId);

    List<GameUser> findByUserId(Long userId);
}
