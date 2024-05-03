package com.demo.KIDING.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReq {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    private String phone;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
