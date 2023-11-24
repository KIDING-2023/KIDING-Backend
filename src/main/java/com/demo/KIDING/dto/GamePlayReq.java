package com.demo.KIDING.dto;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class GamePlayReq {

    private String name;
    private Long userId;
}
