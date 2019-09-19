package com.poiner.redis.demo.spring;

import java.lang.annotation.*;

/**
 * 标识非Spring自动注入的属性
 *
 * @author Du Ping
 * @date 2019-04-19
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {
}
