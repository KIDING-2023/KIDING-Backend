package com.demo.KIDING.controller;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.FriendRequestReq;
import com.demo.KIDING.dto.FriendRequestRes;
import com.demo.KIDING.service.FriendRequestService;
import com.demo.KIDING.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping("/request")
    public void sendFriendRequest(@RequestBody FriendRequestReq request) {
        System.out.println("/api/friends/request");
        friendRequestService.sendFriendRequest(request.getSenderNickname(), request.getReceiverNickname());
    }

    @PostMapping("/respond")
    public String respondToFriendRequest(@RequestBody FriendRequestRes friendRequestRes) {
        System.out.println("Controller: FriendRequestRes.isAccepted = " + friendRequestRes.getIsAccepted());
        friendRequestService.respondToFriendRequest(
                friendRequestRes.getSenderNickname(),
                friendRequestRes.getReceiverNickname(),
                friendRequestRes.getIsAccepted()
        );
        return friendRequestRes.getIsAccepted() ? "Friend request accepted" : "Friend request rejected";
    }

}
