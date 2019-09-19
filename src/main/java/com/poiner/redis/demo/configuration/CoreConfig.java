package com.poiner.redis.demo.configuration;

import com.poiner.redis.demo.utils.idgenerator.Snowflake;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SnowflakeProperties.class
})
public class CoreConfig {

    @Bean
    @ConditionalOnClass(Snowflake.class)
    @ConditionalOnMissingBean
    Snowflake snowflake(SnowflakeProperties properties) {
        return new Snowflake(properties.getIdc(), properties.getMachine());
    }

}
