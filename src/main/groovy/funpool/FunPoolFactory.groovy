package funpool

import com.funtester.base.interfaces.IPooled
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject

/**
 * 可池化工厂类
 */
abstract class FunPoolFactory extends BasePooledObjectFactory<IPooled> {

    abstract IPooled init()

    @Override
    IPooled create() throws Exception {
        init()
    }

    @Override
    PooledObject<IPooled> wrap(IPooled obj) {
        return obj.reInit()
    }

    @Override
    void destroyObject(PooledObject<IPooled> p) throws Exception {
        p.getObject().destory()
        super.destroyObject(p)
    }
}
