package com.poiner.redis.demo.utils.idgenerator;

/**
 * TimeBackException
 *
 */
public class ClockBackException extends RuntimeException {

    public ClockBackException() {
        super("Clock moved backwards.  Refusing to generate id!");
    }
}
