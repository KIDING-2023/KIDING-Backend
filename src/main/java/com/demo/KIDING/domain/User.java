package com.demo.KIDING.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "user")
@Entity
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    private boolean activated;

    @Enumerated(EnumType.STRING)
    private Role role;

    private int answers;  // 누적 대답수

    private int score;  // 1위 경험횟수

    private int players_with;  // 함께한 친구

    private int kiding_chip;  // 키딩칩

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks;  // 즐겨찾기


}
