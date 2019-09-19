package com.poiner.redis.demo.aspect;

import com.poiner.redis.demo.exception.GetDistributeLockException;
import com.poiner.redis.demo.utils.idgenerator.Snowflake;
import com.poiner.redis.demo.utils.redis.RedisTool;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;

@Component
@Aspect
public class RedisLockAdvice {

    private static final Logger logger = LoggerFactory.getLogger(RedisLockAdvice.class);

    private final Jedis jedis;
    private final Snowflake snowflake;

    @Autowired
    public RedisLockAdvice(Jedis jedis, Snowflake snowflake) {
        this.jedis = jedis;
        this.snowflake = snowflake;
    }

    @Around("@annotation(RedisLockAnnoation)")
    public Object processAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上的注释对象
        String methodName =  joinPoint.getSignature().getName();
        Class<?> classTarget = joinPoint.getTarget().getClass();
        Class<?>[] par = ((MethodSignature)joinPoint.getSignature()).getParameterTypes();
        Method objectMethod = classTarget.getMethod(methodName, par);
        RedisLockAnnoation redisLockAnnoation = objectMethod.getDeclaredAnnotation(RedisLockAnnoation.class);

        //拼装分布式锁的key
        String[] keys = redisLockAnnoation.keys();
        String redisKey = redisLockAnnoation.keyPrefix() + String.join("_", keys);
        String requestId = snowflake.nextId() + "";

        // 执行分布式锁的逻辑
        if(redisLockAnnoation.isSpin()) {
            //阻塞锁
            int lockRetryTime = 0;
            int expireTime = redisLockAnnoation.expireTime();
            try {
                while (!RedisTool.tryGetDistributeLock(jedis, redisKey, requestId, expireTime)) {
                    if(lockRetryTime++ > redisLockAnnoation.retryTimes()) {
                        logger.error("lock exception. key:{}, lockRetryTime:{}", redisKey, lockRetryTime);
                        throw new GetDistributeLockException(redisKey);
                    }
                    Thread.sleep(redisLockAnnoation.waitTime());
                }
                return joinPoint.proceed();
            } finally {
                RedisTool.releaseDistributeLock(jedis, redisKey, requestId);
            }
        } else {
            //非阻塞锁
            try {
                if (!RedisTool.tryGetDistributeLock(jedis, redisKey, requestId)) {
                    logger.error("lock exception. key:{}", redisKey);
                    throw new GetDistributeLockException(redisKey);
                }
                return joinPoint.proceed();
            } finally {
                RedisTool.releaseDistributeLock(jedis, redisKey, requestId);
            }
        }
    }
}
