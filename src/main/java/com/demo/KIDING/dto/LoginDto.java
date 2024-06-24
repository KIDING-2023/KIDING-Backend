package com.demo.KIDING.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginDto {

    private Long id;

    private String token;

    private String nickname;
}
