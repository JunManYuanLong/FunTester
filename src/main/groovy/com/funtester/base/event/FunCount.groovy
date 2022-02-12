package com.funtester.base.event

import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
/**
 * 计数器
 */
class FunCount extends SourceCode implements Runnable {

    private static final Logger logger = LogManager.getLogger(FunCount.class);

    /**
     * 性能测试计数器构造方法
     * @param start 初始值
     * @param step 步长
     * @param interval 间隔
     * @param time 测试时间
     * @param max 最大值
     * @param name 名称
     */
    FunCount(int start, int step, int interval, int time, int max, String name) {
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
            count = count >= max ? max : count + step
        }
        stop()
    }

    /**
     * 中止,结束
     * @return
     */
    def stop() {
        status = false
        output("启动器结束了")
    }

    /**
     * 获取event保有量
     * @return int有助于times语法使用
     */
    int getQps() {
        count
    }

    /**
     * 提高最大QPS,用于动态模型压测
     * @param qps
     * @return
     */
    def add(int qps) {
        max += qps
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
