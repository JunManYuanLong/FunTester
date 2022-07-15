package com.funtester.frame.execute

import com.funtester.base.event.FunCount
import com.funtester.config.Constant
import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.LongAdder

/**
 * 基于事件的静态QPS模型
 */
class FunEventConcurrent extends SourceCode {

    private static final Logger logger = LogManager.getLogger(FunEventConcurrent.class);

    /**
     * 执行器,一般使用缓存线程池实现
     */
    static ThreadPoolExecutor executor

    /**
     * 请求计数器,用于打印实际QPS
     */
    LongAdder total = new LongAdder()

    /**
     * 计数器
     */
    FunCount funcount

    /**
     * 任务
     */
    Closure produce

    /**
     * 用例名称,继承于{@link com.funtester.frame.execute.FunEventConcurrent#funcount}
     */
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
