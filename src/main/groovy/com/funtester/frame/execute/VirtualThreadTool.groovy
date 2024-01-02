package com.funtester.frame.execute

import com.funtester.config.Constant
import com.funtester.frame.SourceCode

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
/**
 * 虚拟线程工具类
 */
class VirtualThreadTool extends Constant {

    /**
     * 待执行任务队列,最大容量为MAX_WAIT_TASK
     */
    static LinkedBlockingQueue<Closure> queue = new LinkedBlockingQueue(MAX_WAIT_TASK)

    /**
     * 线程技术,用于控制正在运行的虚拟线程数量
     */
    static AtomicInteger index = new AtomicInteger()

    /**
     * 添加任务
     * @param closure
     * @return
     */
    static def add(Closure closure) {
        queue.add(closure)
    }

    /**
     * 执行任务
     * @param runnable
     * @return
     */
    static def execute(Runnable runnable) {
        daemon()
        Thread.startVirtualThread {
            index.getAndIncrement()
            SourceCode.noError {
                runnable.run()
            }
            index.getAndDecrement()
        }
    }

    /**
     * 执行任务
     * @param closure 任务闭包
     * @return
     */
    static def execute(Closure closure) {
        daemon()
        Thread.startVirtualThread {
            index.getAndIncrement()
            SourceCode.noError {
                closure()
            }
            index.getAndDecrement()
        }
    }

    /**
     * daemon线程状态,保障只执行一次
     * @param closure
     * @return
     */
    static AtomicBoolean DaemonState = new AtomicBoolean(false)

    /**
     * 最大并发执行任务数量
     */
    static int MAX_THREAD = 10

    /**
     * 执行daemon线程,保障main方法结束后关闭线程池
     * @return
     */
    static def daemon() {
        def set = DaemonState.getAndSet(true)
        if (set) return
        new Thread(new Runnable() {

            @Override
            void run() {
                SourceCode.noError {
                    while (ThreadPoolUtil.checkMain()) {
                        while (index.get() < MAX_THREAD) {
                            def poll = queue.poll(100, TimeUnit.MILLISECONDS)
                            if (poll != null) {
                                execute(poll)
                            } else {
                                break
                            }
                        }
                        sleep(0.3)
                    }
                }
            }
        }, "FV").start()
    }

}
