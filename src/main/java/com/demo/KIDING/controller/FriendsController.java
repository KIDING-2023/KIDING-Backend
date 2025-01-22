package com.demo.KIDING.controller;

import com.demo.KIDING.dto.MyFriendRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.service.FriendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    // 친구 프로필 조회
    @GetMapping("/friends/{userId}/{friendId}")
    public BaseResponse<MyFriendRes> getFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            return new BaseResponse<>(friendsService.getFriends(userId, friendId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/friends/delete/{userId}/{friendId}")
    public BaseResponse<String> deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            return new BaseResponse<>(friendsService.deleteFriend(userId, friendId));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

