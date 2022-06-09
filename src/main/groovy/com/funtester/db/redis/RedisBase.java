package com.funtester.db.redis;

import com.funtester.config.Constant;
import com.funtester.utils.Join;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;
import redis.clients.jedis.params.XReadParams;
import redis.clients.jedis.params.XTrimParams;
import redis.clients.jedis.resps.StreamEntry;

import java.util.*;

/**
 * redis测试基础类,不做业务处理
 */
public class RedisBase {

    private static Logger logger = LogManager.getLogger(RedisBase.class);

    String host;

    int port;

    JedisPool pool;

    String auth;

    /**
     * 从0开始
     */
    public int index;

    public RedisBase() {
    }

    public RedisBase(String host, int port) {
        this.host = host;
        this.port = port;
        pool = RedisPool.getPool(host, port);
    }

    public RedisBase(String host, int port, String auth) {
        this.host = host;
        this.port = port;
        this.auth = auth;
        pool = StringUtils.isBlank(auth) ? RedisPool.getPool(host, port) : RedisPool.getPool(host, port, auth);
    }

    /**
     * 获取jedis操作对象，回收资源方法close，3.0以后废弃了其他方法，默认连接第一个数据库
     * 默认使用0个index
     *
     * @return
     */
    public Jedis getJedis() {
        Jedis resource = pool.getResource();
        resource.select(index);
        return resource;
    }

    /**
     * 设置key的有效期，单位是秒
     *
     * @param key
     * @param exTime
     * @return
     */
    public boolean expire(String key, int exTime) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, exTime) == 1;
        } catch (Exception e) {
            logger.error("expire key:{} error", key, e);
            return false;
        }
    }

    /**
     * 设置key-value，过期时间
     *
     * @param key
     * @param value
     * @param expiredTime 单位s
     * @return
     */
    public boolean set(String key, String value, int expiredTime) {
        try (Jedis jedis = getJedis()) {
            return jedis.setex(key, expiredTime, value).equalsIgnoreCase("OK");
        } catch (Exception e) {
            logger.error("setex key:{} value:{} error", key, value, e);
            return false;
        }
    }

    /**
     * 设置redis内容
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value).equalsIgnoreCase("OK");
        } catch (Exception e) {
            logger.error("set key:{} value:{} error", key, value, e);
            return false;
        }
    }


    /**
     * 将key对应的value 加1
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        return incr(key, 1);
    }

    /**
     * 将key对应的value 加 n
     *
     * @param key
     * @param num
     * @return
     */
    public Long incr(String key, int num) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(key, num);
        } catch (Exception e) {
            logger.error("setex key:{} value:{} error", key, e);
            return null;
        }
    }

    /**
     * 将key对应的value 减 1
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        return decr(key, 1);
    }

    /**
     * 将key对应的value 减 n
     *
     * @param key
     * @param num
     * @return
     */
    public Long decr(String key, int num) {
        try (Jedis jedis = getJedis()) {
            return jedis.decrBy(key, num);
        } catch (Exception e) {
            logger.error("setex key:{} value:{} error", key, e);
            return null;
        }
    }

    /**
     * 获取value
     *
     * @param key
     * @return
     */
    public String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 设置redis list内容
     * Redis Lpush 命令将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误
     *
     * @param key
     * @param list
     * @return list长度
     */
    public Long lpush(String key, String... list) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpush(key, list);
        } catch (Exception e) {
            return 0l;
        }
    }


    /**
     * 将一个或多个值插入到已存在的列表头部，列表不存在时操作无效
     *
     * @param key
     * @param list
     * @return
     */
    public Long lpushx(String key, String... list) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpushx(key, list);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return 0l;
        }
    }

    /**
     * 将一个或多个值插入到列表的尾部(最右边)。
     * <p>
     * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param list
     * @return
     */
    public Long rpush(String key, String... list) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpush(key, list);
        } catch (Exception e) {
            return 0l;
        }
    }

    /**
     * 用于将一个或多个值插入到已存在的列表尾部(最右边)。如果列表不存在，操作无效
     *
     * @param key
     * @param list
     * @return
     */
    public Long rpushx(String key, String... list) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpushx(key, list);
        } catch (Exception e) {
            return 0l;
        }
    }

    /**
     * 从数组中获取第一个值,并删除该值
     *
     * @param key
     * @return
     */
    public String lpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpop(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 用于移除并返回列表的最后一个元素
     *
     * @param key
     * @return
     */
    public String rpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpop(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 获取数组长度
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.llen(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return 0l;
        }
    }

    /**
     * 获取固定索引的值
     *
     * @param key
     * @param index 从0开始
     * @return
     */
    public String lindex(String key, long index) {
        try (Jedis jedis = getJedis()) {
            return jedis.lindex(key, index);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 获取list某一段
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return new ArrayList<>();
        }
    }

    /**
     * 设置list中index的值
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public String lset(String key, long index, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lset(key, index, value);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 将哈希表 key 中的域 field 的值设为 value
     *
     * @param key
     * @param hkey
     * @param value
     * @return 行数
     */
    public Long hset(String key, String hkey, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hset(key, hkey, value);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回哈希表 key 中给定域 field 的值
     *
     * @param key
     * @param hkey
     * @return
     */
    public String hget(String key, String hkey) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, hkey);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
     *
     * @param key
     * @param hkey
     * @return
     */
    public Long hdel(String key, String hkey) {
        try (Jedis jedis = getJedis()) {
            return jedis.hdel(key, hkey);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 查看哈希表 key 中，给定域 field 是否存在
     *
     * @param key
     * @param hkey
     * @return
     */
    public Boolean hexists(String key, String hkey) {
        try (Jedis jedis = getJedis()) {
            return jedis.hexists(key, hkey);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回哈希表 key 中的所有域
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回哈希表 key 中所有域的值
     *
     * @param key
     * @return
     */
    public List<String> hvals(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hvals(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回哈希表 key 中域的数量
     *
     * @param key
     * @return
     */
    public Long hlen(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hlen(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回哈希表 key 中，所有的域和值
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 为哈希表 key 中的域 field 的值加上增量 increment。如果域 field 不存在，域的值先被初始化为 0
     *
     * @param key
     * @param hkey
     * @param num
     * @return
     */
    public Long hincrBy(String key, String hkey, long num) {
        try (Jedis jedis = getJedis()) {
            return jedis.hincrBy(key, hkey, num);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 为哈希表 key 中的域 field 的值加上增量 increment。如果域 field 不存在，域的值先被初始化为 0
     *
     * @param key
     * @param hkey
     * @param num
     * @return
     */
    public Double hincrByFloat(String key, String hkey, double num) {
        try (Jedis jedis = getJedis()) {
            return jedis.hincrByFloat(key, hkey, num);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }


    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
     *
     * @param key
     * @param value
     * @return
     */
    public Long sadd(String key, String... value) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回集合 key 的基数(集合中元素的数量)
     *
     * @param key
     * @return
     */
    public Long scard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回集合 key 中的所有成员。 不存在的 key 被视为空集合
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 移除并返回集合中的一个随机元素
     *
     * @param key
     * @return
     */
    public String spop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.spop(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。
     * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
     *
     * @param key
     * @param count
     * @return
     */
    public List<String> srandmember(String key, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.srandmember(key, count);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 返回集合中的一个随机元素
     *
     * @param key
     * @return
     */
    public String srandmember(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.srandmember(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
     *
     * @param key
     * @param value
     * @return
     */
    public Long srem(String key, String... value) {
        try (Jedis jedis = getJedis()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        }
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("exists key:{} error", key, e);
            return false;
        }
    }

    /**
     * 删除key
     * jedis返回值1表示成功，0表示失败，可能是不存在的key
     *
     * @param key
     * @return
     */
    public Long del(String... key) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(key);
        } catch (Exception e) {
            logger.error("del key:{} error", key, e);
            return null;
        }
    }

    /**
     * 获取key对应value的类型
     *
     * @param key
     * @return
     */
    public String type(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.type(key);
        } catch (Exception e) {
            logger.error("type key:{} error", key, e);
            return null;
        }
    }

    /**
     * 获取符合条件的key集合
     *
     * @param pattern
     * @return
     */
    public Set<String> getKeys(String pattern) {
        try (Jedis jedis = getJedis()) {
            return jedis.keys(pattern);
        } catch (Exception e) {
            logger.error("type key:{} error", pattern, e);
            return new HashSet<String>();
        }
    }

    /**
     * 将 value 追加到 key 原来的值的末尾
     *
     * @param key
     * @param content
     * @return
     */
    public Long append(String key, String content) {
        try (Jedis jedis = getJedis()) {
            return jedis.append(key, content);
        } catch (Exception e) {
            logger.error("append key:{} ,content:{},error", key, content, e);
            return null;
        }
    }


    /**
     * 添加stream key内容
     *
     * @param key
     * @param xAddParams
     * @param msg
     * @return
     */
    public StreamEntryID xadd(String key, XAddParams xAddParams, Map<String, String> msg) {
        try (Jedis jedis = getJedis()) {
            return jedis.xadd(key, xAddParams, msg);
        } catch (Exception e) {
            logger.error("stream key:{} xadd error", key, e);
            return null;
        }
    }

    /**
     * 修剪stream ,实质是创建stream内存,可通过xadd方法设置maxleng实现,但是xadd性能不够好
     *
     * @param key
     * @param xTrimParams
     * @return
     */
    public long trim(String key, XTrimParams xTrimParams) {
        try (Jedis jedis = getJedis()) {
            return jedis.xtrim(key, xTrimParams);
        } catch (Exception e) {
            logger.error("stream key:{} trim error", key, e);
            return Constant.TEST_ERROR_CODE;
        }
    }

    /**
     * 读取stream消息
     * 通过xReadParams中的count和block来控制读取数量和阻塞时间
     * 常用{@link StreamEntryID#LAST_ENTRY}获取改时间点之后的消息,默认会获取历史信息,从最开始获取
     *
     * @param xReadParams Map<String, StreamEntryID> entry = ["fun": new StreamEntryID()]
     * @param entry
     * @return
     */
    public List<Map.Entry<String, List<StreamEntry>>> xread(XReadParams xReadParams, Map<String, StreamEntryID> entry) {
        try (Jedis jedis = getJedis()) {
            return jedis.xread(xReadParams, entry);
        } catch (Exception e) {
            logger.error("stream key:{} xread error", Join.join(entry.keySet(), Constant.COMMA), e);
            return null;
        }
    }

    /**
     * 获取某个返回内的消息列表
     *
     * @param key
     * @param start {@link redis.clients.jedis.StreamEntryID},time-sequence
     * @param end   {@link redis.clients.jedis.StreamEntryID},time-sequence
     * @return
     */
    public List<StreamEntry> xrange(String key, String start, String end) {
        try (Jedis jedis = getJedis()) {
            return jedis.xrange(key, start, end);
        } catch (Exception e) {
            logger.error("stream key:{} trim error", key, e);
            return null;
        }
    }

    /**
     * 反向获取消息列表，ID 从大到小
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<StreamEntry> xrevrange(String key, String start, String end) {
        try (Jedis jedis = getJedis()) {
            return jedis.xrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("stream key:{} trim error", key, e);
            return null;
        }
    }

    /**
     * 关闭连接池
     */
    public void close() {
        pool.close();
    }

}
