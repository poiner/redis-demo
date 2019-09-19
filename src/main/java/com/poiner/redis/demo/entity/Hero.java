package com.poiner.redis.demo.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "poiner_hero", indexes = {
        @Index(name = "idx_poiner_hero", columnList = "name")
})
@Getter
@Setter
@EntityListeners({
        AuditingEntityListener.class
})
@EqualsAndHashCode(of = {"id"})
public class Hero implements Serializable {
    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.poiner.redis.demo.jpa.SnowflakeIdGenerator")
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "created_time", updatable = false)
    @CreatedDate
    private Date createdTime;

    @Column(name = "last_modify_time")
    @LastModifiedDate
    private Date lastModifyTime;
}
