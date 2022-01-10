package com.funtester.frame.replay

import com.funtester.frame.SourceCode

import java.util.concurrent.atomic.AtomicInteger

class FunStart extends SourceCode implements Runnable {

    int start

    int max

    int rate

    int interval

    int time

    int count

    AtomicInteger total = new AtomicInteger(0)

    boolean status = true

    @Override
    void run() {
        def st = getMark()
        count = this.start
        while (status) {
            if (getMark() - st > time) break
            sleep(interval as double)
            count += rate
            total.getAndSet(count)
        }
        output("启动器结束了")
        status = false
    }

    def stop() {
        status = false
    }

    def getQps() {
        total.getAndSet(0)
    }

    def start() {
        def thread = new Thread(this)
        def name = thread.setName("FunStart")
    }

}
