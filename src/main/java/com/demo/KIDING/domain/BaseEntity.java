package com.demo.KIDING.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(nullable = false, name = "created_date")
    private Timestamp createdDate;  // 생성 시간

    @LastModifiedDate
    @Column(nullable = false, name = "modified_date")
    private Timestamp modifiedDate;  // 최근 수정 시간
}
