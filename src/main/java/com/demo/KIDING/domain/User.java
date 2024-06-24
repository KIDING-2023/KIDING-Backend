package com.demo.KIDING.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    public void addUserAuthority() {
        this.role = Role.USER;
    }


    private String profile;

    private int answers;  // 누적 대답수

    private int score;  // 1위 경험횟수

    private int players_with;  // 함께한 친구

    private int kiding_chip;  // 키딩칩

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks;  // 즐겨찾기

    @OneToOne(mappedBy = "user")
    private Ranking ranking;

    public void playGame() {
        this.answers += 1;
        this.kiding_chip += 1;
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
