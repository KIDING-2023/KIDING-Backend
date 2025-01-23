package com.demo.KIDING.repository;

import com.demo.KIDING.domain.FriendRequest;
import com.demo.KIDING.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findByReceiverIdAndIsAcceptedFalse(Long receiverId);


}
