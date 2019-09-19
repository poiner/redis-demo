package com.poiner.redis.demo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poiner.redis.demo.config.RedisConfig;
import com.poiner.redis.demo.spring.json.ObjectMapperFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * WebConfigAdapter
 *
 * @author poiner
 */
@Configuration
@EnableWebMvc
@EnableConfigurationProperties({
        RedisConfig.class
})
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    @Primary
    public Jedis jedis(RedisConfig redisConfig) {
        Jedis jedis = new Jedis(redisConfig.getHost(), redisConfig.getPort());
        jedis.auth(redisConfig.getPassword());

        return jedis;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        // 创建缺省的ObjectMapper，提供动态过滤功能
        return ObjectMapperFactory.getDefaultObjectMapper();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods("POST", "PUT", "GET", "DELETE", "OPTIONS")
                .allowedOrigins("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 替换ObjectMapper
        converters.forEach(c -> {
            if (c instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) c).setObjectMapper(objectMapper());
            }
        });
    }

}
