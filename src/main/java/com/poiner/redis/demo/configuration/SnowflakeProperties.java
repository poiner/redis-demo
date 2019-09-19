package com.poiner.redis.demo.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FrameworkProperties
 *
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "matech.framework.snowflake")
class SnowflakeProperties {
    /**
     * 机房ID
     */
    private Integer idc;

    /**
     * 机器ID
     */
    private Integer machine;
}
