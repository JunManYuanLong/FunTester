package com.funtester.frame.replay

import com.funtester.base.constaint.FunThread
import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.WorkHandler

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class EventThread<F> extends FunThread<F> implements EventHandler<FunEvent<F>>, WorkHandler<FunEvent<F>> {

    static AtomicInteger index = new AtomicInteger(0)

    def q = new LinkedBlockingQueue<F>()

    Closure exec

    EventThread(Closure closure) {
        super(null,"consumer${index.getAndIncrement()}")
        this.exec = closure
    }

    @Override
    protected void doing() throws Exception {
        exec(q.poll(500, TimeUnit.MILLISECONDS))
    }

    @Override
    FunThread clone() {
        return new EventThread(f)
    }

    @Override
    void onEvent(FunEvent<F> event) throws Exception {
        if (q.size() < 1000) q.add(event.getEvent())
    }

    @Override
    void onEvent(FunEvent<F> event, long sequence, boolean endOfBatch) throws Exception {
        if (q.size() < 1000) q.add(event.getEvent())
    }


    static class FunEvent<F> {

        F event

    }
}
