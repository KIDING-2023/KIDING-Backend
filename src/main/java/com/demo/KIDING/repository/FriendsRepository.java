package com.demo.KIDING.repository;

import com.demo.KIDING.domain.FriendRequest;
import com.demo.KIDING.domain.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    List<Friends> findByFromUserIdAndIsAcceptedTrue(Long fromUserId);

    Optional<Friends> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    Optional<Friends> findByFromUserIdAndToUserIdAndIsAcceptedTrue(Long fromUserId, Long toUserId);

}
