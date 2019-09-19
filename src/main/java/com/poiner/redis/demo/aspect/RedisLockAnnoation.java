package com.poiner.redis.demo.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLockAnnoation {
    String keyPrefix() default "";

    /**
     * 要锁定的key中包含的属性
     */
    String[] keys() default {};

    /**
     * 是否阻塞锁
     * 1、true：获取不到锁，阻塞一定时间
     * 2、false：获取不到锁，立即返回
     */
    boolean isSpin() default true;

    /**
     * 超时时间
     */
    int expireTime() default 10000;

    /**
     * 等待时间
     */
    int waitTime() default 50;

    /**
     * 重试次数
     */
    int retryTimes() default 20;
}
