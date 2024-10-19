package com.demo.KIDING.dto;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class GamePlayReq {

    private Long boardGameId;
    private Integer count;
}
