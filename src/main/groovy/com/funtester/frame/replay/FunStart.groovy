package com.funtester.frame.replay

import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.atomic.AtomicInteger

class FunStart extends SourceCode implements Runnable {

    private static final Logger logger = LogManager.getLogger(FunStart.class);

    String name

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
        stop()
    }

    def stop() {
        status = false
        total.getAndSet(TEST_ERROR_CODE)
        output("启动器结束了")
    }

    def getQps() {
        total.getAndSet(0)
    }

    def start() {
        def thread = new Thread(this)
        thread.setName(name)
        thread.start()
        logger.info("异步计数器 $name 启动了!")
    }

}
