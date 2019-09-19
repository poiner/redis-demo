package com.poiner.redis.demo.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.Date;

public class RedisTool {
    private static final Logger logger = LoggerFactory.getLogger(RedisTool.class);

    private static final String LOCK_SUCCESS = "OK";

    private static final Long RELEASE_SUCCESS = 1L;

    //锁默认超时时间,20秒
    private static final int DEFAULT_EXPIRE_TIME = 20 * 1000;

    private static final String AUTO_CODE_PREFIX = "AUTO_CODE_";

    public static boolean tryGetDistributeLock(Jedis jedis, String lockKey, String requestId) {
        return tryGetDistributeLock(jedis, lockKey, requestId, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributeLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, new SetParams().nx().px(expireTime));

        logger.debug("获取分布式锁：{}", result);
        return LOCK_SUCCESS.equals(result);
    }

    /**
     * 释放分布式锁
     * @param jedis redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributeLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        logger.debug("释放分布式锁：{}", result);
        return RELEASE_SUCCESS.equals(result);
    }

    /**
     * 获取自增编码
     * @param jedis Redis客户端
     * @param codeType 自增类型
     * @return 编码
     */
    public static long getAutoCodeByRedis(Jedis jedis, String codeType) {
        String script = "return redis.call('incr', KEYS[1])";
        Object result = jedis.eval(script, Collections.singletonList(AUTO_CODE_PREFIX + codeType), Collections.emptyList());
        return Long.valueOf(result.toString());
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.0.5", 6379);
        System.out.println("设置密码：" + jedis.auth("matech.2019"));

        System.out.println("连接成功");

        for(int i = 0 ; i < 100 ; i++) {
            System.out.println(new Date() + ":" + getAutoCodeByRedis(jedis, "BUDGET_APPLY_AUTO_CODE"));
        }
        /*
        String lockKey = "MQG_LOCK";
        String requestId = "1";
        if(tryGetDistributeLock(jedis, lockKey, requestId, 600)) {
            System.out.println("获取锁成功");
            System.out.println("其他人请求锁：" + tryGetDistributeLock(jedis, lockKey, "4", 600));
            System.out.println("其他人释放锁：" + releaseDistributeLock(jedis, lockKey, "4"));
            System.out.println("自己又请求锁：" + tryGetDistributeLock(jedis, lockKey, requestId, 600));
            System.out.println("释放锁：" + releaseDistributeLock(jedis, lockKey, requestId));
        }
        */
    }
}
