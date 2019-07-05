package com.fun.db.redis;

import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class RedisUtil extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 设置key的有效期，单位是秒
     *
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            logger.error("expire key:{} error", key, e);
        } finally {
            jedis.close();
            return result;
        }
    }

    /**
     * 设置key-value，过期时间
     *
     * @param key
     * @param value
     * @param exTime 过期时间，单位s
     * @return
     */
    public static String set(String key, String value, int expiredTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, expiredTime, value);
        } catch (Exception e) {
            logger.error("setex key:{} value:{} error", key, value, e);
        } finally {
            jedis.close();
            return result;
        }
    }

    /**
     * 设置redis内容
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = RedisPool.getJedis();
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error("set key:{} value:{} error", key, value, e);
        } finally {
            jedis.close();
            return EMPTY;
        }
    }

    /**
     * 获取value
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
        } finally {
            jedis.close();
            return result;
        }
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPool.getJedis();
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
        } finally {
            jedis.close();
            return false;
        }
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            logger.error("del key:{} error", key, e);
        } finally {
            jedis.close();
            return result;
        }
    }
}
