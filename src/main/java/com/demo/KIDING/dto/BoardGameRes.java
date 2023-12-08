package com.demo.KIDING.dto;

import com.demo.KIDING.domain.BoardGame;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardGameRes {

    private String name;
    private Long players;
//    private boolean bookmark;


    public static BoardGameRes from(BoardGame boardGame) {
        return BoardGameRes.builder()
                .name(boardGame.getName())
                .players(boardGame.getPlayers())
                .build();
    }

}
