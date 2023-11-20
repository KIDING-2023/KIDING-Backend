package com.demo.KIDING.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "user")
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    private int answers;  // 누적 대답수

    private int score;  // 1위 경험횟수

    private int players_with;  // 함께한 친구

    private int kiding_chip;  // 키딩칩

}
