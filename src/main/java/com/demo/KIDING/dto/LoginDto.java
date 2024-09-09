package com.demo.KIDING.dto;

import com.demo.KIDING.domain.Role;
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
    private String password;
    private Role role;
}
