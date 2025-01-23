package com.demo.KIDING.repository;

import com.demo.KIDING.domain.FriendRequest;
import com.demo.KIDING.domain.Friends;
import com.demo.KIDING.dto.FriendInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {

   @Query("""
        SELECT CASE WHEN f.fromUser.id = :userId THEN f.toUser.id ELSE f.fromUser.id END
        FROM Friends f
        WHERE (f.fromUser.id = :userId OR f.toUser.id = :userId) AND f.isAccepted = true
    """)
    List<Long> findFriendIdsByUserId(@Param("userId") Long userId);

    Optional<Friends> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    Optional<Friends> findByFromUserIdAndToUserIdAndIsAcceptedTrue(Long fromUserId, Long toUserId);

}
