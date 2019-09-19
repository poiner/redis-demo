package com.poiner.redis.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {
    private String host;

    private Integer port;

    private String password;
}
