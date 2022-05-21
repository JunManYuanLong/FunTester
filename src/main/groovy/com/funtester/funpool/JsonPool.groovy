package com.funtester.funpool

import com.alibaba.fastjson.JSONObject
import com.funtester.config.PoolConstant
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class JsonPool extends PoolConstant {

    private static final Logger logger = LogManager.getLogger(JsonPool.class);

    private static GenericObjectPool<JSONObject> pool

    static def init() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(MAX);
        poolConfig.setMinIdle(MIN_IDLE);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxWaitMillis(MAX_WAIT_TIME);
        poolConfig.setMinEvictableIdleTimeMillis(MAX_IDLE_TIME);
        pool = new GenericObjectPool<>(new FunTester(), poolConfig);
    }

    /**
     * 获取{@link JSONObject}对象
     * @return
     */
    static JSONObject get() {
        try {
            return pool.borrowObject()
        } catch (e) {
            logger.warn("获取${JSONObject.class} 失败", e)
        } finally {
            new JSONObject()
        }
    }

    /**
     * 归还{@link JSONObject}对象
     * @param JSONObject
     * @return
     */
    static def back(JSONObject JSONObject) {
        JSONObject.clear()
        pool.returnObject(JSONObject)
    }

    /**
     * 执行任务
     * @param closure
     * @return
     */
    static def execute(Closure closure) {
        def get = get()
        try {
            closure(get)
        } catch (e) {
            logger.warn("执行任务失败", e)
        } finally {
            back(get)
        }

    }

    private static class FunTester extends BasePooledObjectFactory<JSONObject> {

        @Override
        JSONObject create() throws Exception {
            return new JSONObject()
        }

        @Override
        PooledObject<JSONObject> wrap(JSONObject obj) {
            return new DefaultPooledObject<JSONObject>(obj)
        }

        @Override
        void destroyObject(PooledObject<JSONObject> p) throws Exception {
            def json = p.getObject()
            json.clear()
            super.destroyObject(p)
        }
    }
}
