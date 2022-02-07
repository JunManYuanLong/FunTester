package com.funtester.base.event

import com.funtester.frame.SourceCode
import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.WorkHandler

import java.util.concurrent.atomic.AtomicInteger

/**
 * 基于消息实现的多线程,Disruptor消费者
 * @param <F>
 */
class EventThread<F> implements EventHandler<FunEvent<F>>, WorkHandler<FunEvent<F>> {

    static AtomicInteger index = new AtomicInteger(0)

    /**
     * 实际执行方法,用于消费消息的方法
     */
    Closure exec

    /**
     * 消费者名称,默认递增
     */
    String name

    /**
     * 构造方法,传入闭包,不考虑Java的兼容性
     * @param closure
     */
    EventThread(Closure closure) {
        this.exec = closure
        name = "C-${SourceCode.formatInt(index.getAndIncrement(), 4)}"
    }

    /**
     * 多消费者
     * @param event
     * @throws Exception
     */
    @Override
    void onEvent(FunEvent<F> event) throws Exception {
        exec(event.getEvent())
    }

    /**
     * 单消费者
     * @param event
     * @param sequence
     * @param endOfBatch
     * @throws Exception
     */
    @Override
    void onEvent(FunEvent<F> event, long sequence, boolean endOfBatch) throws Exception {
        exec(event.getEvent())
    }


    /**
     * 消息体
     * @param <F>
     */
    static class FunEvent<F> {

        F event

    }
}
