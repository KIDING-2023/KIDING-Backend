package com.demo.KIDING.controller;

import com.demo.KIDING.dto.FriendRequestReq;
import com.demo.KIDING.dto.FriendRequestRes;
import com.demo.KIDING.dto.RequestBoxRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.jwt.JwtProvider;
import com.demo.KIDING.service.FriendRequestService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final JwtProvider jwtProvider;

//    @PostMapping("/request")
//    public void sendFriendRequest(@RequestBody FriendRequestReq request) {
//        System.out.println("/api/friends/request");
//        friendRequestService.sendFriendRequest(request.getSenderNickname(), request.getReceiverNickname());
//    }

    // 친구 신청 보내기
    @PostMapping("/request")
    public void sendFriendRequest(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FriendRequestReq request) {
        System.out.println("/api/friends/request");

        // 토큰에서 sender 정보 추출
        Claims claims = jwtProvider.parseClaims(accessToken);
        String senderNickname = claims.get("nickname", String.class);

        // 친구 신청 보내기
        friendRequestService.sendFriendRequest(senderNickname, request.getReceiverNickname());
    }

    // 친구 신청 응답 처리
    @PostMapping("/respond")
    public String respondToFriendRequest(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody FriendRequestRes friendRequestRes) {
        System.out.println("Controller: FriendRequestRes.isAccepted = " + friendRequestRes.getIsAccepted());

        // 토큰에서 receiver 정보 추출
        Claims claims = jwtProvider.parseClaims(accessToken);
        String receiverNickname = claims.get("nickname", String.class);

        // 친구 요청 응답 처리
        friendRequestService.respondToFriendRequest(
                friendRequestRes.getSenderNickname(),
                receiverNickname,
                friendRequestRes.getIsAccepted()
        );

        return friendRequestRes.getIsAccepted() ? "Friend request accepted" : "Friend request rejected";
    }

//    @PostMapping("/respond")
//    public String respondToFriendRequest(@RequestBody FriendRequestRes friendRequestRes) {
//        System.out.println("Controller: FriendRequestRes.isAccepted = " + friendRequestRes.getIsAccepted());
//        friendRequestService.respondToFriendRequest(
//                friendRequestRes.getSenderNickname(),
//                friendRequestRes.getReceiverNickname(),
//                friendRequestRes.getIsAccepted()
//        );
//        return friendRequestRes.getIsAccepted() ? "Friend request accepted" : "Friend request rejected";
//    }

    /**
     * 받은 친구 요청 확인 API
     * @param accessToken 사용자 토큰
     * @return 받은 친구 요청 목록
     */
    @GetMapping("/request-box")
    public BaseResponse<List<RequestBoxRes>> getReceivedFriendRequests(
            @RequestHeader(value = "Authorization") String accessToken) {
        try {
            List<RequestBoxRes> requests = friendRequestService.getReceivedFriendRequests(accessToken);
            return new BaseResponse<>(requests);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
