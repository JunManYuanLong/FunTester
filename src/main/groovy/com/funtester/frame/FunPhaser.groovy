package com.funtester.frame

import java.util.concurrent.atomic.AtomicInteger

/**
 * 自定义同步类,避免{@link java.util.concurrent.Phaser}的不足,总数量受限于65535
 * 用于多线程任务同步,任务完成后,调用{@link #done()}方法,任务总数减少,当任务总数为0时,调用{@link #await()}方法,等待所有任务完成
 */
class FunPhaser extends SourceCode {

    /**
     * 任务总数索引,用于标记任务完成状态
     * 注册增加,任务完成减少
     */
    AtomicInteger index

    /**
     * 任务总数,用于记录任务完成数量
     */
    AtomicInteger taskNum

    FunPhaser() {
        this.index = new AtomicInteger()
        this.taskNum = new AtomicInteger()
    }

    /**
     * 注册任务
     * @return
     */
    def register() {
        this.index.getAndIncrement()
    }

    /**
     * 任务完成
     * @return
     */
    def done() {
        this.index.getAndDecrement()
        this.taskNum.getAndIncrement()
    }

    /**
     * 等待所有任务完成
     * @return
     */
    def await() {
        waitFor {index.get() == 0}
    }

    /**
     * 获取任务完成总数
     * @return
     */
    int queryTaskNum() {
        return taskNum.get()
    }

}