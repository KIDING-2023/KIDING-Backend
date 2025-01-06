package com.demo.KIDING.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "ranking")
@Entity
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rankingPosition;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;  // 랭킹과 일대일 관계

    private int chips;

}
