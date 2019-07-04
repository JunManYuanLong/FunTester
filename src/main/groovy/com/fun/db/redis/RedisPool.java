package com.fun.db.redis;

import com.fun.frame.SourceCode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool extends SourceCode {

    private static JedisPool pool = initPool();

    ;//jedis连接池

    private static int maxTotal = 10; //最大连接数

    private static int maxIdle = 5;//在jedispool中最大的idle状态(空闲的)的jedis实例的个数

    private static int minIdle = 2;//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static boolean testOnBorrow = true;//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。

    private static boolean testOnReturn = true;//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static boolean blockWhenExhausted = true;//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。

    private static String redisIp = "10.10.4.103";

    private static int redisPort = 29200;


    /**
     * 初始化连接池
     */
    private static JedisPool initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(blockWhenExhausted);
        return pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    /**
     * 获取jedis操作对象，回收资源方法close，3.0以后废弃了其他方法
     *
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 关闭连接池资源
     */
    public static void close() {
        pool.close();
    }
}
