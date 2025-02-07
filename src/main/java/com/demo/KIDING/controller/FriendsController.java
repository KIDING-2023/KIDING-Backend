package com.demo.KIDING.controller;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.FriendInfo;
import com.demo.KIDING.dto.MyFriendRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.common.BaseResponseStatus;
import com.demo.KIDING.global.jwt.JwtProvider;
import com.demo.KIDING.repository.UserRepository;
import com.demo.KIDING.service.FriendsService;
import com.demo.KIDING.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    // 친구 프로필 조회
    @GetMapping("/friends/{userId}/{friendId}")
    public BaseResponse<MyFriendRes> getFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            return new BaseResponse<>(friendsService.getFriends(userId, friendId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/friends/delete/{friendId}")
    public BaseResponse<String> deleteFriend(@RequestHeader("Authorization") String token, @PathVariable String friendNickname) {
        try {

            // 토큰 파싱하여 nickname 추출
            Claims claims = jwtProvider.parseClaims(token);
            String username = claims.get("nickname", String.class);

            // nickname으로 User 조회
            Optional<User> optionalUser = userRepository.findByNickname(username);
            if (optionalUser.isEmpty()) {
                throw new BaseException(BaseResponseStatus.NO_USER_FOUND);
            }

            User friend = userRepository.findByNickname(friendNickname)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_USER_FOUND));

            Long userId = optionalUser.get().getId();

            return new BaseResponse<>(friendsService.deleteFriend(userId, friend.getId()));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/friends/list")
    public BaseResponse<List<FriendInfo>> getFriendsList(@RequestHeader("Authorization") String accessToken) {
        try {
            List<FriendInfo> friendsList = friendsService.getFriendsList(accessToken);
            return new BaseResponse<>(friendsList);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());

        }
    }
}

