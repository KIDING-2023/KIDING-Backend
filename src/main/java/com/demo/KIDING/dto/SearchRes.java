package com.demo.KIDING.dto;

import lombok.*;
import org.hibernate.type.EntityType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRes<T> {

    private Long id;
    private String name;
    private String entityTypeValue;  // BoardGame, Friends
    private String image;  // user일때만 존재하는 필드

    public SearchRes(Long id, String name, String entityTypeValue) {
        this.id = id;
        this.name = name;
        this.entityTypeValue = entityTypeValue;
    }

    public SearchRes(Long id, String name, String entityTypeValue, String image) {
        this.id = id;
        this.name = name;
        this.entityTypeValue = entityTypeValue;
        this.image = image;
    }
}
