package com.demo.KIDING.repository;

import com.demo.KIDING.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);
    boolean existsById(Long userId);
}
