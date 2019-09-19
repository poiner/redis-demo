package com.poiner.redis.demo.module;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class HeroDTO extends HeroBaseDTO implements Serializable {
    private Date createdTime;

    private Date lastModifyTime;
}
