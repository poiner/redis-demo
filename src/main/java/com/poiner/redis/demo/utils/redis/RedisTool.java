package com.poiner.redis.demo.utils.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisTool {

    private static final Long RELEASE_SUCCESS = 1L;

    //锁默认超时时间,20秒
    private static final int DEFAULT_EXPIRE_TIME = 20 * 1000;

    private static final String AUTO_CODE_PREFIX = "AUTO_CODE_";

    /**
     * 尝试获取分布式锁
     *
     * @param redisTemplate redis操作模版
     * @param lockKey       锁
     * @param requestId     请求标识
     * @return 是否获取成功
     */
    public static Boolean tryGetDistributeLock(RedisTemplate<Object, Object> redisTemplate, String lockKey, String requestId) {
        return tryGetDistributeLock(redisTemplate, lockKey, requestId, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 尝试获取分布式锁
     * @param redisTemplate redis操作模版
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static Boolean tryGetDistributeLock(RedisTemplate<Object, Object> redisTemplate, String lockKey, String requestId, int expireTime) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, Duration.ofMillis(expireTime));
        log.info("获取分布式锁：{}", result);
        return result;
    }

    /**
     * 释放分布式锁
     * @param redisTemplate redis操作模版
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static Boolean releaseDistributeLock(RedisTemplate<Object, Object> redisTemplate, String lockKey, String requestId) {
        RedisScript<Object> redisScript = new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Object.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);

        log.info("释放分布式锁：{}" , result);
        return RELEASE_SUCCESS.equals(result);
    }

    /**
     * 获取自增编码
     * @param redisTemplate redis操作模版
     * @param codeType 自增类型
     * @return 编码
     */
    public static Long getAutoCodeByRedis(RedisTemplate<Object, Object> redisTemplate, String codeType) {
        return redisTemplate.opsForValue().increment(AUTO_CODE_PREFIX + codeType);
    }

}
