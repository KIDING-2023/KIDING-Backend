package com.demo.KIDING.controller;

import com.demo.KIDING.domain.FriendRequest;
import com.demo.KIDING.dto.MessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 친구신청 버튼 클릭(friends/request) -> /app/friends/request로 클라이언트가 메시지 전송
    // SimpMessagingTemplate를 사용해서 destination으로 메시지 전송

    @MessageMapping("/friends/request")
    public void sendFriendRequest(FriendRequest request) {
        String destination = "/topic/notifications/" + request.getReceiver().getNickname();
        messagingTemplate.convertAndSend(destination, new MessageDto(request.getSender().getNickname() + "님이 친구 신청을 요청했습니다."));

    }

}
