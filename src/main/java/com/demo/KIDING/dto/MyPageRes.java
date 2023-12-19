package com.demo.KIDING.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageRes {

    private String nickname;

    private int answers;

    private int score;  // 1위 경험

    private int players_with;

    private int kiding_chip;

    // 동점자 추가

}
