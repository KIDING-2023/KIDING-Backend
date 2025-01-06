package com.demo.KIDING.service;

import com.demo.KIDING.domain.FriendRequest;
import com.demo.KIDING.domain.Friends;
import com.demo.KIDING.domain.Notification;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.MessageDto;
import com.demo.KIDING.repository.FriendRequestRepository;
import com.demo.KIDING.repository.FriendsRepository;
import com.demo.KIDING.repository.NotificationRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final SimpMessagingTemplate messagingTemplate;

    private final FriendRequestRepository friendRequestRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;


    @Transactional
    public FriendRequest sendFriendRequest(String senderNickname, String receiverNickname) {
        User sender = userRepository.findByNickname(senderNickname)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender nickname"));
        User receiver = userRepository.findByNickname(receiverNickname)
                .orElseThrow(() -> new IllegalArgumentException("Invalid receiver nickname"));

        System.out.println(senderNickname + receiverNickname);

        // 이미 친구 요청이 존재하는지 확인
        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (existingRequest.isPresent()) {
            throw new IllegalStateException("이미 친구 요청을 보냈습니다.");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .isAccepted(false)  // 초기 상태는 수락되지 않은 상태x
                .build();
        // 요청 저장 & 알림 전송
        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        notificationService.sendFriendRequestNotification(receiverNickname, senderNickname);

        return savedRequest;
    }

    @Transactional
    public void respondToFriendRequest(String senderNickname, String receiverNickname, boolean isAccepted) {

        User sender = userRepository.findByNickname(senderNickname)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender nickname"));
        User receiver = userRepository.findByNickname(receiverNickname)
                .orElseThrow(() -> new IllegalArgumentException("Invalid receiver nickname"));

        System.out.println("Calling respondToFriendRequest...");

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new IllegalStateException("친구 요청이 존재하지 않습니다."));


        if (isAccepted) {
            Friends friendship = Friends.builder()
                    .fromUser(sender)
                    .toUser(receiver)
                    .isAccepted(true)
                    .build();
            friendsRepository.save(friendship);

            // 수락 알림 전송
            notificationService.sendFriendRequestResponseNotification(senderNickname, receiverNickname, "accepted");
        } else {
            notificationService.sendFriendRequestResponseNotification(senderNickname, receiverNickname, "rejected");
        }

        // 요청 삭제
        friendRequestRepository.delete(friendRequest);

    }

    // 친구 신청 수락 (디버그 로그 추가)
//    @Transactional
//    public void respondToFriendRequest(String senderNickname, String receiverNickname, boolean isAccepted) {
//        System.out.println("Method respondToFriendRequest called with:");
//        System.out.println("Sender Nickname: " + senderNickname);
//        System.out.println("Receiver Nickname: " + receiverNickname);
//        System.out.println("isAccepted: " + isAccepted);
//
//        // 사용자 조회
//        User sender = userRepository.findByNickname(senderNickname)
//                .orElseThrow(() -> {
//                    System.err.println("Invalid sender nickname: " + senderNickname);
//                    return new IllegalArgumentException("Invalid sender nickname");
//                });
//
//        User receiver = userRepository.findByNickname(receiverNickname)
//                .orElseThrow(() -> {
//                    System.err.println("Invalid receiver nickname: " + receiverNickname);
//                    return new IllegalArgumentException("Invalid receiver nickname");
//                });
//
//        // 친구 요청 조회
//        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
//                .orElseThrow(() -> {
//                    System.err.println("Friend request not found for sender: " + senderNickname + ", receiver: " + receiverNickname);
//                    return new IllegalStateException("Friend request does not exist");
//                });
//
//        System.out.println("Friend request found: " + friendRequest);
//
//        // 수락 또는 거절 처리
//        if (isAccepted) {
//            System.out.println("Processing friend request as accepted...");
//            // 친구 관계 생성 및 저장
//            Friends friendship = Friends.builder()
//                    .fromUser(sender)
//                    .toUser(receiver)
//                    .isAccepted(true)
//                    .build();
//
//            Friends savedFriendship = friendsRepository.save(friendship);
//            System.out.println("Friendship saved: " + savedFriendship);
//
//            // 수락 알림 전송
//            notificationService.sendFriendRequestResponseNotification(senderNickname, receiverNickname, "accepted");
//            System.out.println("Notification sent for accepted friend request.");
//        } else {
//            System.out.println("Processing friend request as rejected...");
//            // 거절 알림 전송
//            notificationService.sendFriendRequestResponseNotification(senderNickname, receiverNickname, "rejected");
//            System.out.println("Notification sent for rejected friend request.");
//        }
//
//        // 친구 요청 삭제
//        friendRequestRepository.delete(friendRequest);
//        System.out.println("Friend request deleted for sender: " + senderNickname + ", receiver: " + receiverNickname);
//    }


//        public void acceptFriendRequest(Long requestId) {
//        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
//        friendRequest.requestReply(false);
//        friendRequestRepository.save(friendRequest);
//
//        Friends friendship = Friends.builder()
//                .fromUser(friendRequest.getSender())
//                .toUser(friendRequest.getReceiver())
//                .isAccepted(true)
//                .build();
//        friendsRepository.save(friendship);
//
//        Notification notification = Notification.builder()
//                .message(friendRequest.getReceiver().getNickname() + "님이 친구요청을 수락했습니다.")
//                .receiver(friendRequest.getSender())
//                .read(false)
//                .build();
//        notificationRepository.save(notification);
//
//        messagingTemplate.convertAndSendToUser(friendRequest.getSender().getNickname(), "/queue/notifications", notification);
//    }

}



