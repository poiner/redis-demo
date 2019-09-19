package com.poiner.redis.demo.kafka.module;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Messages {
    private Long id;

    private String msg;

    private Date sendTime;
}
