package com.demo.KIDING.service;

import com.demo.KIDING.domain.FriendRequest;
import com.demo.KIDING.domain.Friends;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.RequestBoxRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.jwt.JwtProvider;
import com.demo.KIDING.repository.FriendRequestRepository;
import com.demo.KIDING.repository.FriendsRepository;
import com.demo.KIDING.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.demo.KIDING.global.common.BaseResponseStatus.NO_USER_FOUND;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final RankingService rankingService;
    private final JwtProvider jwtProvider;


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

    @Transactional(readOnly = true) // 친구 신청 확인
    public List<RequestBoxRes> getReceivedFriendRequests(String accessToken) throws BaseException {
        // Access Token에서 사용자 정보 추출
        Claims claims = jwtProvider.parseClaims(accessToken);
        String username = claims.get("nickname", String.class);

        // 사용자 조회
        User receiver = userRepository.findByNickname(username)
                .orElseThrow(() -> new BaseException(NO_USER_FOUND));

        // 친구 요청 조회
        List<FriendRequest> requests = friendRequestRepository.findByReceiverIdAndIsAcceptedFalse(receiver.getId());

        // 응답 DTO로 변환
        return requests.stream()
                .map(request -> {
                    User sender = request.getSender();
                    int senderRank = rankingService.calculateUserRanking(sender.getId());

                    return RequestBoxRes.builder()
                            .requestId(request.getId())
                            .senderNickname(sender.getNickname())
                            .senderProfile(sender.getProfile())
                            .senderRank(senderRank) // 랭킹 없으면 -1 반환
                            .build();
                })
                .collect(Collectors.toList());
    }
}



