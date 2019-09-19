package com.poiner.redis.demo.spring.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Json工厂类，获取缺省的ObjectMapper
 *
 */
public class ObjectMapperFactory {

    private ObjectMapperFactory() {
    }

    /**
     * 用于创建动态过滤的ObjectMapper, 这样就可以在controller上使用@Json来动态过滤属性
     *
     * @return app缺省配置的objectMapper
     */
    public static ObjectMapper getDefaultObjectMapper() {
        return new ObjectMapper()
                .registerModule(new Hibernate5Module())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .setTimeZone(TimeZone.getTimeZone("GMT+8"))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .addMixIn(Object.class, DynamicFilterMixIn.class)
                .setFilterProvider(new DynamicFilterProvider());
    }
}
