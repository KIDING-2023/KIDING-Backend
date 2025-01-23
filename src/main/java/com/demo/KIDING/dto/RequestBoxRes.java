package com.demo.KIDING.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestBoxRes {

    private Long requestId;
    private String senderNickname;
    private String senderProfile;
    private int senderRank;
}
