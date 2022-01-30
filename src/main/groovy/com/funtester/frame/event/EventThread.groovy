package com.funtester.frame.event

import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.WorkHandler

import java.util.concurrent.atomic.AtomicInteger

class EventThread<F> implements EventHandler<FunEvent<F>>, WorkHandler<FunEvent<F>> {

    static AtomicInteger index = new AtomicInteger(0)

    Closure exec

    EventThread(Closure closure) {
        this.exec = closure
    }

    @Override
    void onEvent(FunEvent<F> event) throws Exception {
        exec(event.getEvent())
    }

    @Override
    void onEvent(FunEvent<F> event, long sequence, boolean endOfBatch) throws Exception {
        exec(event.getEvent())
    }


    static class FunEvent<F> {

        F event

    }
}
