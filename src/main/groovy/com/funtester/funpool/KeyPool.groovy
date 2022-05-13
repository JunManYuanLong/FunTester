package com.funtester.funpool

import com.funtester.base.interfaces.IPooled
import org.apache.commons.pool2.impl.GenericKeyedObjectPool
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

class KeyPool {

    KeyPool(KeyPoolFactory factory) {
        this.factory = factory
        this.pool = init()
    }

    private GenericObjectPool<IPooled> pool = init();

    private KeyPoolFactory<String> factory

    private GenericKeyedObjectPool<String, IPooled> init() {
        // 连接池的配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 池中的最大连接数
        poolConfig.setMaxTotal(8);
        // 最少的空闲连接数
        poolConfig.setMinIdle(0);
        // 最多的空闲连接数
        poolConfig.setMaxIdle(8);
        // 当连接池资源耗尽时,调用者最大阻塞的时间,超时时抛出异常 单位:毫秒数
        poolConfig.setMaxWaitMillis(-1);
        // 连接池存放池化对象方式,true放在空闲队列最前面,false放在空闲队列最后
        poolConfig.setLifo(true);
        // 连接空闲的最小时间,达到此值后空闲连接可能会被移除,默认即为30分钟
        poolConfig.setMinEvictableIdleTimeMillis(1000L * 60L * 30L);
        // 连接耗尽时是否阻塞,默认为true
        poolConfig.setBlockWhenExhausted(true);
        // 连接池创建
        return new GenericKeyedObjectPool<String, IPooled>(factory, poolConfig);
    }

    /**
     * 获取对象
     */
    IPooled get() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factory.create();
    }

    /**
     * 归还对象
     * @param iPooled
     */
    void back(IPooled iPooled) {
        pool.returnObject(iPooled)
    }

    /**
     * 执行器
     */
    def execute(Closure closure) {
        IPooled client = get();
        try {
            closure(client);
        } finally {
            pool.returnObject(client);
        }
    }

}
