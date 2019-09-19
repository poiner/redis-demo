package com.poiner.redis.demo.spring.json;

import java.lang.annotation.*;

/**
 * Json注解，用于注解在Controller的方法上，动态过滤不需要的属性
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Jsons.class)
public @interface Json {

    /**
     * 需要检查过滤的类
     */
    Class<?> type();

    /**
     * 需要包括的属性，用逗号分隔
     */
    String include() default "";

    /**
     * 需要过滤的属性，用逗号分隔
     */
    String filter() default "";
}
