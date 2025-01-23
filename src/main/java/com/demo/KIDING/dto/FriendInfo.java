package com.demo.KIDING.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendInfo {
    private String nickname;
    private String profile; // 프로필 이미지 URL 또는 경로
    private int rank;       // 현재 순위
}
