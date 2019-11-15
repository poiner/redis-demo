package com.poiner.redis.demo.schedule;

import com.poiner.redis.demo.exception.GetDistributeLockException;
import com.poiner.redis.demo.utils.redis.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author maqionggui
 * @date 2019/9/22
 */
@Component
public class RedisSchedule {
    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public RedisSchedule(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 */2 * * * ?")
    public void autoCodeSchedule() {
        String requestId = UUID.randomUUID().toString();
        String redisKey = "1_lockKey";
        //非阻塞锁
        try {
            if (!RedisTool.tryGetDistributeLock(redisTemplate, redisKey, requestId)) {
                throw new GetDistributeLockException(redisKey);
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            RedisTool.releaseDistributeLock(redisTemplate, redisKey, requestId);
        }
        /*
        for(int i = 0 ; i < 100 ; i++) {
            System.out.println(new Date() + ":" + RedisTool.getAutoCodeByRedis(redisTemplate, "BUDGET_APPLY_AUTO_CODE"));
        }
        */
    }
}
