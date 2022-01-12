package com.funtester.frame.replay

import com.funtester.base.constaint.FixedThread
import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.WorkHandler

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class ReplayThread<F> extends FixedThread implements EventHandler<FunEvent>, WorkHandler<FunEvent> {

    def q = new LinkedBlockingQueue<F>()

    ReplayThread(Closure closure) {
        super(closure, 0, true)
    }

    @Override
    protected void doing() throws Exception {
        f(q.poll(100, TimeUnit.MILLISECONDS))
    }

    @Override
    FixedThread clone() {
        return new ReplayThread(f)
    }

    @Override
    void onEvent(FunEvent event) throws Exception {
        if (q.size() < 1000) q.add(event.getEvent())
    }

    @Override
    void onEvent(FunEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (q.size() < 1000) q.add(event.getEvent())
    }


    static class FunEvent<F> {

        F event

    }
}
