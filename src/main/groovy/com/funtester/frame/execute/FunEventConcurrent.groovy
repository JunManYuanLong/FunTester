package com.funtester.frame.execute

import com.funtester.base.event.FunCount
import com.funtester.config.Constant
import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.LongAdder

class FunEventConcurrent extends SourceCode {

    private static final Logger logger = LogManager.getLogger(FunEventConcurrent.class);

    static ThreadPoolExecutor executor

    static int count_interval = 5

    LongAdder total = new LongAdder()

    FunCount funcount

    Closure produce

    String name

    FunEventConcurrent(Closure closure, FunCount funCount) {
        this.funcount = funCount
        this.produce = closure
        this.name = funcount.name
    }

    void start() {
        if (executor == null) executor = ThreadPoolUtil.createCachePool(Constant.THREADPOOL_MAX, "E")
        funcount.start()
        while (funcount.status) {
            ThreadPoolUtil.executeTask(executor, funcount.getQps(), produce, total, name)
        }
        stop()
    }

    /**
     * 中止
     * @return
     */
    def stop() {
        executor.shutdown()
        logger.info("FunEvent压测关闭了!")
    }

}
