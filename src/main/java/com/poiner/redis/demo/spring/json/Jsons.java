package com.poiner.redis.demo.spring.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Json的数组方式注解，用于注解在Controller的方法上，动态过滤不需要的属性
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Jsons {
    Json[] value();
}
