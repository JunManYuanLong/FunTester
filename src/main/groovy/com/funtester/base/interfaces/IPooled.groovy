package com.funtester.base.interfaces

import org.apache.commons.pool2.PooledObject

interface IPooled {

    PooledObject<IPooled> reInit()

    void destory()

}
