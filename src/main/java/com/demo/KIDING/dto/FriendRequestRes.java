package com.demo.KIDING.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestRes {
    private String senderNickname;
    private String receiverNickname;
    private boolean isAccepted;

    public boolean getIsAccepted() {
        return isAccepted;
    }
}
