package com.funtester.frame.execute


import com.funtester.base.exception.FailException
import com.funtester.config.Constant
import com.funtester.frame.SourceCode
import com.funtester.base.event.EventThread
import com.funtester.base.event.FunStart
import com.lmax.disruptor.RingBuffer
import com.lmax.disruptor.YieldingWaitStrategy
import com.lmax.disruptor.dsl.Disruptor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.stream.Collectors

class EventConcurrent<F> extends SourceCode {

    private static final Logger logger = LogManager.getLogger(EventConcurrent.class);

    static boolean key = true

    static int grid = 10

    Disruptor<EventThread.FunEvent<F>> disruptor

    RingBuffer<EventThread.FunEvent<F>> ringBuffer

    FunStart fs

    List<Closure> ts

    EventConcurrent(FunStart fs, List<EventThread<F>> ts) {
        this.fs = fs
        this.ts = ts
        check()
        start()
    }

    def check() {
        if (fs.getStart() % grid + fs.getStep() % grid != 0) logger.warn("QPS参数不匹配,必须是 $grid 的整数倍")
    }

    /**
     *
     * 关于{@link com.lmax.disruptor.WaitStrategy}参数的说明
     *         BlockingWaitStrategy	加锁	CPU资源紧缺，吞吐量和延迟并不重要的场景
     *         BusySpinWaitStrategy	自旋	通过不断重试，减少切换线程导致的系统调用，而降低延迟。推荐在线程绑定到固定的CPU的场景下使用
     *         PhasedBackoffWaitStrategy	自旋 + yield + 自定义策略	CPU资源紧缺，吞吐量和延迟并不重要的场景
     *         SleepingWaitStrategy	自旋 + yield + sleep	性能和CPU资源之间有很好的折中。延迟不均匀
     *         TimeoutBlockingWaitStrategy	加锁，有超时限制	CPU资源紧缺，吞吐量和延迟并不重要的场景
     *         YieldingWaitStrategy	自旋 + yield + 自旋	性能和CPU资源之间有很好的折中。延迟比较均匀
     */
    public void start() {
        disruptor = new Disruptor<EventThread.FunEvent>(
                EventThread.FunEvent::new,
                16 * 16,
                ThreadPoolUtil.getFactory("E"),
                com.lmax.disruptor.dsl.ProducerType.MULTI,
                new YieldingWaitStrategy()
        );
        ringBuffer = disruptor.getRingBuffer()
        def consumers = range(CONCUMER_SIZE).mapToObj(f -> new EventThread<F>(random(ts))).collect(Collectors.toList())
        disruptor.handleEventsWithWorkerPool(consumers as EventThread<F>[])
        disruptor.start()
        fs.start()
    }


    /**
     * 中止
     * @return
     */
    def stop() {
        fs.stop()
        if (disruptor != null) disruptor.shutdown()
    }

    /**
     * 结束所有的,只中止生产者
     * @return
     */
    static def stopAll() {
        key = false
    }

    /**
     * 生产
     * @param produce
     */
    def send(Closure<F> produce) {
        if (produce() == null) FailException.fail("生产者类型错误")
        if (ThreadPoolUtil.getFunPool().activeCount > Constant.POOL_SIZE - 5) {
            logger.warn("生产者线程繁忙,丢弃改任务")
            return
        }
        while (key) {
            sleep(1.0)
            def qps = fs.getQps()
            if (qps == 0) continue
            if (qps < 0) break
            fun {
                grid.times {
                    sleep(1.0 / grid)
                    (qps / grid).times {
                        send(produce())
                    }
                }
            }

        }
    }

    /**
     * 生产对象
     * @param f
     * @return
     */
    def send(F f) {
        ringBuffer.publishEvent {e, s ->
            {
                e.setEvent(f)
            }
        }
    }

}
