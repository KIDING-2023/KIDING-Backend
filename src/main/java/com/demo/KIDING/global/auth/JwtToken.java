package com.demo.KIDING.global.auth;

import lombok.*;

@Builder
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
