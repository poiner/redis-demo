package com.poiner.redis.demo.exception;

public class GetDistributeLockException extends RuntimeException{
    /**
     * 构造器
     * @param lockKey 锁
     */
    public GetDistributeLockException(String lockKey) {
        super(String.format("获取(%s)锁异常", lockKey));
    }
}
