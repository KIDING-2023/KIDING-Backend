package com.demo.KIDING.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentGameRes {

    private String name;
    private Long players;
    private Timestamp time;
}
