package com.demo.KIDING.service;

import com.demo.KIDING.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendFriendRequestNotification(String receiverNickname, String senderNickname) {
        String destination = "/topic/notifications/" + receiverNickname;

        MessageDto messageDto = new MessageDto(senderNickname+ "님이 친구 신청을 요청했습니다.");
        messagingTemplate.convertAndSend(destination, messageDto);

        // 메시지 내용 출력
        System.out.println("Destination: " + destination);
        System.out.println("Message: " + messageDto.getMsg());

    }

    public void sendFriendRequestResponseNotification(String senderNickname, String receiverNickname, String response) {

        try {
            System.out.println("Sending notification: sender=" + senderNickname + ", receiver=" + receiverNickname + ", status=" + response);
            // 알림 전송 로직
            String destination = "/topic/notifications/" + senderNickname;
            String messageContent = receiverNickname+"님이 친구 신청을 " + response +" 수락했습니다.";
            MessageDto messageDto = new MessageDto(messageContent);
            messagingTemplate.convertAndSend(destination, messageDto);
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
            throw e; // 예외 다시 던지기
        }




    }


}
