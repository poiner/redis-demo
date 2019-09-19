package com.poiner.redis.demo.utils.idgenerator;

/**
 * twitter的snowflake算法 -- java实现
 * <p>
 * SnowFlake算法用来生成64位的ID，刚好可以用long整型存储，能够用于分布式系统中生产唯一的ID， 并且生成的ID有大致的顺序。
 * <p>
 * 生成的64位ID可以分成5个部分：
 * <p>
 * 0 - 41位时间戳 - 5位数据中心标识 - 5位机器标识 - 12位序列号
 * <p>
 * 5位数据中心标识跟5位机器标识这样的分配仅仅是当前实现中分配的，如果业务有其实的需要，
 * 可以按其它的分配比例分配，如10位机器标识，不需要数据中心标识。
 *
 */
public final class Snowflake {

    /**
     * 起始的时间戳，从2018年10月1日0时0分0秒开始
     */
    private static final long START_TIMESTAMP = 1538323200000L;
    /**
     * 序列号占用的位数
     */
    private static final long SEQUENCE_BIT = 12;
    /**
     * 机器标识占用的位数
     */
    private static final long MACHINE_BIT = 5;
    /**
     * 数据中心占用的位数
     */
    private static final long IDC_BIT = 5;
    /**
     * 每一部分的最大值
     */
    private static final long MAX_IDC_NUM = ~(-1L << IDC_BIT);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    /**
     * 每一部分向左的位移
     */
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long IDC_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTAMP_LEFT = IDC_LEFT + IDC_BIT;
    private long idcId;
    private long machineId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * 使用IDC及MachineID创建生成器
     *
     * @param idcId     IDC编号
     * @param machineId 机器编号
     */
    public Snowflake(long idcId, long machineId) {
        if (idcId > MAX_IDC_NUM || idcId < 0) {
            throw new IllegalArgumentException("invalid idcId");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("invalid machine id");
        }
        this.idcId = idcId;
        this.machineId = machineId;
    }

    /**
     * 获取ID的指定部分
     *
     * @param id   Snowflake生成的ID
     * @param part 需要解析的部分
     * @return 相关部分对应的值，-1表示有误
     */
    public static long parse(long id, Part part) {
        long result;
        switch (part) {
            case TIMESTAMP:
                result = (id >> TIMESTAMP_LEFT) + START_TIMESTAMP;
                break;
            case IDC:
                result = (id >> IDC_LEFT) & MAX_IDC_NUM;
                break;
            case MACHINE:
                result = (id >> MACHINE_LEFT) & MAX_MACHINE_NUM;
                break;
            case SEQUENCE:
                result = id & MAX_SEQUENCE;
                break;
            default:
                result = -1;
        }
        return result;
    }

    /**
     * 产生分布式主键ID
     *
     * @return 生成的ID
     */
    public synchronized long nextId() {
        long currStamp = getNewTimestamp();
        if (currStamp < lastTimestamp) {
            throw new ClockBackException();
        }

        if (currStamp == lastTimestamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTimestamp = currStamp;
        return ((currStamp - START_TIMESTAMP) << TIMESTAMP_LEFT) | (idcId << IDC_LEFT) | (machineId << MACHINE_LEFT) | sequence;
    }

    private long getNextMill() {
        long mill = getNewTimestamp();
        while (mill <= lastTimestamp) {
            mill = getNewTimestamp();
        }
        return mill;
    }

    private long getNewTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * ID所包含的数据部分
     */
    public enum Part {
        /**
         * 时间戳
         */
        TIMESTAMP,

        /**
         * 机房ID
         */
        IDC,

        /**
         * 机器编号
         */
        MACHINE,

        /**
         * 序列号
         */
        SEQUENCE
    }
}