package com.demo.KIDING.repository;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.RankingRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);
    boolean existsById(Long userId);

    boolean existsByPhone(String phone);

    Optional<User> findByNickname(String nickname);

    User findTopByOrderByAnswersAsc();

    @Query(value = "SELECT u.id, u.nickname, u.answers FROM user u INNER JOIN game_user gu ON u.id = gu.user_id ORDER BY u.answers DESC", nativeQuery = true)
    Optional<List<RankingRes>> findRankUser();

    // JPQL
    @Query("SELECT u FROM User u WHERE u.nickname LIKE %:name%")
    Optional<User> searchByUserNickname(@Param("name")String name);
}
