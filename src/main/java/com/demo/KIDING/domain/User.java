package com.demo.KIDING.domain;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user")
@Entity
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String nickname;

    @NotNull
    private String password;

    @Column(unique = true)
    @NotNull
    private String phone;

    private boolean activated;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profile;

    private int answers;  // 누적 대답수

    private int score;  // 1위 경험횟수

    private int players_with;  // 함께한 친구

    private int kiding_chip;  // 키딩칩

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks;  // 즐겨찾기

    @OneToOne(mappedBy = "user")
    private Ranking ranking;

    public void playGame(Integer count) {
        this.answers += 1;
        this.kiding_chip += count;
    }

    public void setCharacter(Integer num) {
        switch(num) {
            case 1:
                this.profile = "https://kiding-bucket.s3.ap-northeast-2.amazonaws.com/character/Group+13873.png";
                return ;
            case 2:
                this.profile = "https://kiding-bucket.s3.ap-northeast-2.amazonaws.com/character/Group+13887.png";
                return ;
            case 3:
                this.profile = "https://kiding-bucket.s3.ap-northeast-2.amazonaws.com/character/Group+13875.png";
                return ;
            case 4:
                this.profile = "https://kiding-bucket.s3.ap-northeast-2.amazonaws.com/character/Group+13876.png";
        }
    }

}
