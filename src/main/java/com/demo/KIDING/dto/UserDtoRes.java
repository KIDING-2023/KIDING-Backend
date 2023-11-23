package com.demo.KIDING.dto;

import com.demo.KIDING.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRes {

    private Long id;
    private String nickname;
    private String role;

    public static UserDtoRes from(User user) {
        return UserDtoRes.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .role(user.getRole().toString()).build();
    }

}
