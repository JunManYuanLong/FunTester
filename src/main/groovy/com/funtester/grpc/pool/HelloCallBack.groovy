package com.funtester.grpc.pool

interface HelloCallBack<F> {

    void execute(F f)

}