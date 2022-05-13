package com.funtester.funpool

import com.funtester.base.interfaces.IPooled
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory
import org.apache.commons.pool2.PooledObject
/**
 * 可池化工厂类
 */
abstract class KeyPoolFactory<F> extends BaseKeyedPooledObjectFactory<F, IPooled> {

    abstract IPooled init()

    @Override
    IPooled create(F k) throws Exception {
        return init()
    }

    @Override
    PooledObject<IPooled> wrap(IPooled obj) {
        return obj.reInit()
    }

    @Override
    void destroyObject(F key, PooledObject<IPooled> p) throws Exception {
        p.getObject().destory()
        super.destroyObject(key, p)
    }
}
