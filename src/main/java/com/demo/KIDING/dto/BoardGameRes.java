package com.demo.KIDING.dto;

import com.demo.KIDING.domain.BoardGame;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardGameRes {

    private Long id;
    private String name;
    private Long players;
    private boolean bookmarked;


    public static BoardGameRes from(BoardGame boardGame) {
        return BoardGameRes.builder()
                .id(boardGame.getId())
                .name(boardGame.getName())
                .players(boardGame.getPlayers())
                .bookmarked(false)
                .build();
    }

}
