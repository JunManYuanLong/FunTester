package com.funtester.base.event

import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.WorkHandler
/**
 * 基于消息实现的多线程,Disruptor消费者
 * @param <F>
 */
class EventThread<F> implements EventHandler<FunEvent<F>>, WorkHandler<FunEvent<F>> {

    /**
     * 实际执行方法,用于消费消息的方法
     */
    Closure exec

    /**
     * 构造方法,传入闭包,不考虑Java的兼容性
     * @param closure
     */
    EventThread(Closure closure) {
        this.exec = closure
    }

    /**
     * 多消费者
     * @param event
     * @throws Exception
     */
    @Override
    void onEvent(FunEvent<F> event) throws Exception {
        try {
            exec(event.getEvent())
        } catch (e) {

        }
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
        try {
            exec(event.getEvent())
        } catch (e) {

        }
    }


    /**
     * 消息体
     * @param <F>
     */
    static class FunEvent<F> {

        F event

    }
}
