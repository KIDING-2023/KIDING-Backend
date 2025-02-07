package com.demo.KIDING.repository;

import com.demo.KIDING.domain.FriendRequest;
import com.demo.KIDING.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findByReceiverIdAndIsAcceptedFalse(Long receiverId);

    @Modifying
    @Query("DELETE FROM FriendRequest fr WHERE fr.sender = :user OR fr.receiver = :user")
    void deleteAllByUserId(@Param("user") User user);


}
