package com.funtester.frame.event

import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.atomic.AtomicInteger

/**
 * 计数器
 */
class FunStart extends SourceCode implements Runnable {

    private static final Logger logger = LogManager.getLogger(FunStart.class);

    FunStart(int start, int max, int step, int interval, int time, String name) {
        this.name = name
        this.start = start
        this.max = max
        this.step = step
        this.interval = interval
        this.time = time
    }

    /**
     * 启动器名字
     */
    String name

    /**
     * 起始值
     */
    int start

    /**
     * 最大值
     */
    int max

    /**
     * 步长
     */
    int step

    /**
     * 间隔
     */
    int interval

    /**
     * 总时间
     */
    int time

    /**
     * 当前QPS
     */
    int count

    /**
     * 当前可提供event保有量
     */
    AtomicInteger total = new AtomicInteger(0)

    /**
     * 结束标志
     */
    boolean status = true

    @Override
    void run() {
        def st = getMark()
        count = this.start
        while (status) {
            if (getMark() - st > time) break
            sleep(interval as double)
            count += step
            total.addAndGet(count)
        }
        stop()
    }

    /**
     * 中止,结束
     * @return
     */
    def stop() {
        status = false
        total.getAndSet(TEST_ERROR_CODE)
        output("启动器结束了")
    }

    /**
     * 获取event保有量
     * @return
     */
    def getQps() {
        total.getAndSet(0)
    }

    /**
     * 启动计数器
     * @return
     */
    def start() {
        def thread = new Thread(this)
        thread.setName(name)
        thread.start()
        logger.info("异步计数器 $name 启动了!")
    }

}
