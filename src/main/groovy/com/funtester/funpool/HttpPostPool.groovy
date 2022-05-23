package com.funtester.funpool


import com.funtester.config.PoolConstant
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.http.client.methods.HttpPost
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.time.Duration

class HttpPostPool extends PoolConstant {

    private static final Logger logger = LogManager.getLogger(HttpPostPool.class);

    private static GenericObjectPool<HttpPost> pool

    static def init() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(MAX);
        poolConfig.setMinIdle(MIN_IDLE);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxWait(Duration.ofMillis(MAX_WAIT_TIME))
        poolConfig.setMinEvictableIdleTime(Duration.ofMillis(MAX_IDLE_TIME))
        pool = new GenericObjectPool<>(new FunTester(), poolConfig);
    }

    /**
     * 获取{@link org.apache.http.client.methods.HttpPost}对象
     * @return
     */
    static HttpPost get() {
        try {
            return pool.borrowObject()
        } catch (e) {
            logger.warn("获取${HttpPost.class} 失败", e)
            new HttpPost()
        }
    }

    /**
     * 归还{@link org.apache.http.client.methods.HttpPost}对象
     * @param HttpPost
     * @return
     */
    static def back(HttpPost HttpPost) {
        pool.returnObject(HttpPost)
    }

    /**
     * 执行任务
     * @param closure
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

    /**
     * 池化工厂
     */
    private static class FunTester extends BasePooledObjectFactory<HttpPost> {

        @Override
        HttpPost create() throws Exception {
            return new HttpPost()
        }

        @Override
        PooledObject<HttpPost> wrap(HttpPost obj) {
            return new DefaultPooledObject<HttpPost>(obj)
        }

        @Override
        void destroyObject(PooledObject<HttpPost> p) throws Exception {
            def get = p.getObject()
            get.setURI(null)
            super.destroyObject(p)
        }
    }

}
