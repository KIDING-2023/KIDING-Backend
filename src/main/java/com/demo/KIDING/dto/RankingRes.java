package com.demo.KIDING.dto;

import com.demo.KIDING.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingRes {

    private Long id;
    private String nickname;
    private String profileImage;
    private Integer answers;

    public static RankingRes from(User user){
        return RankingRes.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfile())
                .answers(user.getAnswers())
                .build();
    }
}
