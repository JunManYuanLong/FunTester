package com.funtester.db.redis;

public class RedisUtil extends RedisPool {

//    public static int getIndex() {
//        return index;
//    }
//
//    public static void setIndex(int index) {
//        RedisUtil.index = index;
//    }
//
//    /**
//     * redis数据库索引，默认0
//     */
//    private static int index;
//
//    private static Logger logger = LogManager.getLogger(RedisUtil.class);
//
//    /**
//     * 获取jedis操作对象，回收资源方法close，3.0以后废弃了其他方法，默认连接第一个数据库
//     *
//     * @return
//     */
//    public static Jedis getJedis() {
//        return RedisPool.getPool().getResource();
//    }
//
//    /**
//     * 获取某一个database的操作连接
//     *
//     * @param index
//     * @return
//     */
//    public static Jedis getJedis(int index) {
//        Jedis jedis = getJedis();
//        jedis.select(index);
//        return jedis;
//    }
//
//    /**
//     * 设置key的有效期，单位是秒
//     *
//     * @param key
//     * @param exTime
//     * @return
//     */
//    public static boolean expire(String key, int exTime) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.expire(key, exTime) == 1;
//        } catch (Exception e) {
//            logger.error("expire key:{} error", key, e);
//            return false;
//        }
//    }
//
//    /**
//     * 设置key-value，过期时间
//     *
//     * @param key
//     * @param value
//     * @param expiredTime
//     * @return
//     */
//    public static String set(String key, String value, int expiredTime) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.setex(key, expiredTime, value);
//        } catch (Exception e) {
//            logger.error("setex key:{} value:{} error", key, value, e);
//            return EMPTY;
//        }
//    }
//
//    /**
//     * 设置redis内容
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public static String set(String key, String value) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.set(key, value);
//        } catch (Exception e) {
//            logger.error("set key:{} value:{} error", key, value, e);
//            return EMPTY;
//        }
//    }
//
//    /**
//     * 获取value
//     *
//     * @param key
//     * @return
//     */
//    public static String get(String key) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.get(key);
//        } catch (Exception e) {
//            logger.error("get key:{} error", key, e);
//            return EMPTY;
//        }
//    }
//
//    /**
//     * 是否存在key
//     *
//     * @param key
//     * @return
//     */
//    public static boolean exists(String key) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.exists(key);
//        } catch (Exception e) {
//            logger.error("exists key:{} error", key, e);
//            return false;
//        }
//    }
//
//    /**
//     * 删除key
//     * jedis返回值1表示成功，0表示失败，可能是不存在的key
//     *
//     * @param key
//     * @return
//     */
//    public static boolean del(String key) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.del(key) == 1;
//        } catch (Exception e) {
//            logger.error("del key:{} error", key, e);
//            return false;
//        }
//    }
//
//    /**
//     * 获取key对应value的类型
//     *
//     * @param key
//     * @return
//     */
//    public static String type(String key) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.type(key);
//        } catch (Exception e) {
//            logger.error("type key:{} error", key, e);
//            return EMPTY;
//        }
//    }
//
//    /**
//     * 获取符合条件的key集合
//     *
//     * @param pattern
//     * @return
//     */
//    public static Set<String> getKeys(String pattern) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.keys(pattern);
//        } catch (Exception e) {
//            logger.error("type key:{} error", pattern, e);
//            return new HashSet<String>();
//        }
//    }
//
//    /**
//     * 获取符合条件的key集合
//     *
//     * @param key
//     * @param content
//     * @return
//     */
//    public static boolean append(String key, String content) {
//        try (Jedis jedis = getJedis()) {
//            return jedis.append(key, content) > 0;
//        } catch (Exception e) {
//            logger.error("append key:{} ,content:{},error", key, content, e);
//            return false;
//        }
//    }
}
