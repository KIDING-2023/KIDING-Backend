package com.demo.KIDING.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MyFriendRes {

    private String nickname;
    private String profile;
    private int score;  // 1위 경험횟수

}
