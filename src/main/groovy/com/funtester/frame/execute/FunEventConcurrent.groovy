package com.funtester.frame.execute

import com.funtester.base.event.FunCount
import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.LongAdder

class FunEventConcurrent<F> extends SourceCode {

    private static final Logger logger = LogManager.getLogger(FunEventConcurrent.class);

    static ThreadPoolExecutor executor

    LongAdder total = new LongAdder()

    boolean key = true

    FunCount funcount

    Closure produce

    FunEventConcurrent(FunCount fs, Closure closure) {
        this.funcount = fs
        this.produce = closure
        start()
    }


    void start() {
        executor = ThreadPoolUtil.createPool()
        funcount.start()
        def index = -funcount.getStart() - 1
        while (key) {
            def qps = funcount.getQps()
            if (qps == 0) continue
            if (qps < 0) break
            fun {
                qps.times {
                    if (key) {
                        executor.execute(new Runnable() {

                            @Override
                            void run() {
                                produce()
                                total.increment()
                            }
                        })
                    }
                }
            }
            sleep(1.0)
            logger.info("当前设计QPS:{},实际QPS:{} 活跃线程数:{}", qps, total.sumThenReset(), executor.getActiveCount())
        }
        stop()
    }


    /**
     * 中止
     * @return
     */
    def stop() {
        key = false
        funcount.stop()
        logger.info("FunEvent压测关闭了!")
    }

}
