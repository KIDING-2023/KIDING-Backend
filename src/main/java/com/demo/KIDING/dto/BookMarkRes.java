package com.demo.KIDING.dto;

import com.demo.KIDING.domain.BookMark;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookMarkRes {

    private Long BoardGameId;
    private String name;
    private Long players;

    public static BookMarkRes from(BookMark bookMark) {
        return BookMarkRes.builder()
                .BoardGameId(bookMark.getBoardGame().getId())
                .name(bookMark.getBoardGame().getName())
                .players(bookMark.getBoardGame().getPlayers())
                .build();
    }

}
