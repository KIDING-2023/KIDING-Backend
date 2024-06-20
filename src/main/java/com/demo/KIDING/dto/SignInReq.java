package com.demo.KIDING.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInReq {

    private String nickname;
    private String password;
}
