package com.funtester.funpool


import com.funtester.config.PoolConstant
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.http.client.methods.HttpGet
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.time.Duration

class HttpGetPool extends PoolConstant {

    private static final Logger logger = LogManager.getLogger(HttpGetPool.class);

    private static GenericObjectPool<HttpGet> pool

    static def init() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(MAX)
        poolConfig.setMinIdle(MIN_IDLE)
        poolConfig.setMaxIdle(MAX_IDLE)
        poolConfig.setMaxWait(Duration.ofMillis(MAX_WAIT_TIME))
        poolConfig.setMinEvictableIdleTime(Duration.ofMillis(MAX_IDLE_TIME))
        pool = new GenericObjectPool<>(new FunTester(), poolConfig)
    }


    /**
     * 获取{@link org.apache.http.client.methods.HttpGet}对象
     * @return
     */
    static HttpGet get() {
        try {
            return pool.borrowObject()
        } catch (e) {
            logger.warn("获取${HttpGet.class} 失败", e)
            new HttpGet()
        }
    }

    /**
     * 归还{@link org.apache.http.client.methods.HttpGet}对象
     * @param httpGet
     * @return
     */
    static def back(HttpGet httpGet) {
        pool.returnObject(httpGet)
    }

    /**
     * 执行任务
     * @param closure
     * @return
     */
    def execute(Closure closure) {
        def get = get()
        try {
            closure(get)
        } catch (e) {
            logger.warn("执行任务失败", e)
        } finally {
            back(get)
        }

    }

    private static class FunTester extends BasePooledObjectFactory<HttpGet> {

        @Override
        HttpGet create() throws Exception {
            return new HttpGet()
        }

        @Override
        PooledObject<HttpGet> wrap(HttpGet obj) {
            return new DefaultPooledObject<HttpGet>(obj)
        }

    }
}
