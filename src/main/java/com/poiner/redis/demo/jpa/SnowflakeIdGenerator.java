package com.poiner.redis.demo.jpa;

import com.poiner.redis.demo.spring.SpringUtil;
import com.poiner.redis.demo.utils.idgenerator.Snowflake;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Hibernate的Snowflake算法的ID生成器
 * <p>
 * Hibernate初始化时将会自动创建生成器
 *
 */
public class SnowflakeIdGenerator implements IdentifierGenerator {

    private Snowflake snowflake;

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        if (snowflake == null) {
            this.snowflake = (Snowflake) SpringUtil.getBean("snowflake");
            if (this.snowflake == null) {
                throw new HibernateException("Cannot create SnowflakeIdGenerator!");
            }
        }

        return snowflake.nextId();
    }
}
