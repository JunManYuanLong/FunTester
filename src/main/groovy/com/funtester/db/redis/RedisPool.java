package com.funtester.db.redis;

import com.alibaba.fastjson.JSONObject;
import com.funtester.frame.SourceCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 */
public class RedisPool extends SourceCode {

    static Logger logger = LogManager.getLogger(RedisPool.class);

    /**
     * 最大连接数
     */
    private static int MAX_TOTAL = 1000;

    /**
     * 在jedispool中最大的idle状态(空闲的)的jedis实例的个数
     */
    private static int MAX_IDLE = 300;

    /**
     * 在jedispool中最小的idle状态(空闲的)的jedis实例的个数
     */
    private static int MIN_IDLE = 10;

    /**
     * 获取实例的最大等待时间
     */
    private static long MAX_WAIT = 5000;

    /**
     * redis连接的超时时间
     */
    private static int TIMEOUT = 5000;

    /**
     * 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的
     */
    private static boolean testOnBorrow = true;

    /**
     * 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
     */
    private static boolean testOnReturn = true;

    /**
     * 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true
     */
    private static boolean blockWhenExhausted = true;

    private static JedisPoolConfig config = getConfig();

    /**
     * 初始化连接池
     *
     * @param host
     * @param port
     * @return
     */
    public static JedisPool getPool(String host, int port) {
        logger.info("redis连接池IP：{}，端口：{}，超时设置：{}", host, port, TIMEOUT);
        return new JedisPool(config, host, port, TIMEOUT);
    }

    /**
     * 初始化连接池,附带密码
     *
     * @param host
     * @param port
     * @param password
     * @return
     */
    public static JedisPool getPool(String host, int port, String password) {
        logger.info("redis连接池IP：{}，端口：{}，超时设置：{}", host, port, TIMEOUT);
        return new JedisPool(config, host, port, TIMEOUT, password);
    }

    /**
     * 默认连接池配置
     *
     * @return
     */
    private static JedisPoolConfig getConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_TOTAL);
        config.setMaxIdle(MAX_IDLE);
        config.setMinIdle(MIN_IDLE);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(blockWhenExhausted);
        config.setMaxWaitMillis(MAX_WAIT);
        logger.debug("连接redis配置：{}", JSONObject.toJSONString(config));
        return config;
    }

}
