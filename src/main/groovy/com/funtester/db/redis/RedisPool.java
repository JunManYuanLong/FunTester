package com.funtester.db.redis;

import com.funtester.frame.SourceCode;

/**
 * redis连接池
 */
public class RedisPool extends SourceCode {

//    static Logger logger = LogManager.getLogger(RedisPool.class);
//
//    static PropertyUtils.Property property = PropertyUtils.getProperties("redis");
//
//    private static String IP = property.getProperty("ip");
//
//    private static int PORT = property.getPropertyInt("port");
//
//    /**
//     * 最大连接数
//     */
//    private static int MAX_TOTAL = property.getPropertyInt("max_total");
//
//    /**
//     * 在jedispool中最大的idle状态(空闲的)的jedis实例的个数
//     */
//    private static int MAX_IDLE = property.getPropertyInt("max_idle");
//
//    /**
//     * 在jedispool中最小的idle状态(空闲的)的jedis实例的个数
//     */
//    private static int MIN_IDLE = property.getPropertyInt("min_idle");
//
//    /**
//     * 获取实例的最大等待时间
//     */
//    private static long MAX_WAIT = property.getPropertyLong("max_wait");
//
//    /**
//     * redis连接的超时时间
//     */
//    private static int TIMEOUT = property.getPropertyInt("timeout");
//
//    /**
//     * 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的
//     */
//    private static boolean testOnBorrow = true;
//
//    /**
//     * 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
//     */
//    private static boolean testOnReturn = true;
//
//    /**
//     * 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true
//     */
//    private static boolean blockWhenExhausted = true;
//
//    private static JedisPoolConfig config = getConfig();
//
//    private static JedisPool pool = initPool();
//
//    /**
//     * 初始化连接池
//     */
//    private static JedisPool initPool() {
//        logger.debug("redis连接池IP：{}，端口：{}，超时设置：{}", IP, PORT, TIMEOUT);
//        return new JedisPool(config, IP, PORT, TIMEOUT);
//    }
//
//    /**
//     * 默认连接池配置
//     *
//     * @return
//     */
//    private static JedisPoolConfig getConfig() {
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(MAX_TOTAL);
//        config.setMaxIdle(MAX_IDLE);
//        config.setMinIdle(MIN_IDLE);
//        config.setTestOnBorrow(testOnBorrow);
//        config.setTestOnReturn(testOnReturn);
//        config.setBlockWhenExhausted(blockWhenExhausted);
//        config.setMaxWaitMillis(MAX_WAIT);
//        logger.debug("连接redis配置：{}", JSONObject.toJSONString(config));
//        return config;
//    }
//
//    /**
//     * 获取连接池
//     *
//     * @return
//     */
//    public static JedisPool getPool() {
//        return pool;
//    }
//
//    /**
//     * 关闭连接池资源
//     */
//    public static void close() {
//        pool.close();
//    }
}
