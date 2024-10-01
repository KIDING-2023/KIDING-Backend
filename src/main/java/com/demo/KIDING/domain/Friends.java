package com.demo.KIDING.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(
        uniqueConstraints = {  // unqiue 설정
                @UniqueConstraint(
                        name="friends",
                        columnNames = {"fromUserId", "toUserId"}
                )
        }
)
@Entity
public class Friends extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="fromUserId")
    private User fromUser;  // 구독하는 유저

    @ManyToOne
    @JoinColumn(name="toUserId")
    private User toUser;  // 구독받는 유저

    private boolean isAccepted;

}
