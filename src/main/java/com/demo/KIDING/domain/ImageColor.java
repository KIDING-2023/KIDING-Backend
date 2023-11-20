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
@Table(name = "image_color")
@Entity
public class ImageColor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    private String color;

    @ManyToOne
//    @JoinColumn(name = "board_game_id")
    private BoardGame boardGame;
}
