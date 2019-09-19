package com.poiner.redis.demo.exception;

/**
 * 无法找到该数据
 */
public class NoSuchDataException extends RuntimeException {
    /**
     * 构造函数
     * @param id ID
     */
    public NoSuchDataException(String id) {
        super(String.format("找不到ID为(%s)的数据", id));
    }
}
