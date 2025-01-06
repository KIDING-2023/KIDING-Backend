package com.demo.KIDING.dto;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class GamePlayReq {

    private Long boardGameId;

   // private String gameName;
    private Long userId;
//
//    // 키딩칩 수
//    private Integer count;
}
