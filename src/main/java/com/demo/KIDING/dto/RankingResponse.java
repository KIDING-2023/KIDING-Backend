package com.demo.KIDING.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingResponse {

    private int rank;
    private String user;
    private int chips;

    public RankingResponse(String user, int chips) {
        this.user = user;
        this.chips = chips;
    }

//    public RankingResponse(String user, int chips, int rank) {
//        this.user = user;
//        this.chips = chips;
//        this.rank = rank;
//    }
}
