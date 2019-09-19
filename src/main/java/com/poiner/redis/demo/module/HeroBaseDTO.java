package com.poiner.redis.demo.module;

import com.poiner.redis.demo.entity.Sex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
class HeroBaseDTO  implements Serializable {
    private String id;

    private String name;

    private Sex sex;
}
