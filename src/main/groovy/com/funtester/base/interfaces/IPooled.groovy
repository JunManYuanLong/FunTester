package com.funtester.base.interfaces

interface IPooled {

    IPooled newInstance()

    IPooled reInit()

    void destory()

}
