package com.funtester.frame.execute

import com.funtester.base.event.EventThread
import com.funtester.base.event.FunCount
import com.funtester.base.exception.FailException
import com.funtester.frame.SourceCode
import com.lmax.disruptor.BlockingWaitStrategy
import com.lmax.disruptor.RingBuffer
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.stream.Collectors

class EventConcurrent<F> extends SourceCode {

    private static final Logger logger = LogManager.getLogger(EventConcurrent.class);

    boolean key = true

    static int grid = 10

    Disruptor<EventThread.FunEvent<F>> disruptor

    RingBuffer<EventThread.FunEvent<F>> ringBuffer

    FunCount fs

    List<EventThread<F>> ts

    int count = 0

    EventConcurrent(FunCount fs, List<EventThread<F>> ts) {
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
     * 所有自旋都超级占用CPU资源
     * 关于{@link com.lmax.disruptor.WaitStrategy}参数的说明
     *         BlockingWaitStrategy	加锁	CPU资源紧缺，吞吐量和延迟并不重要的场景
     *         BusySpinWaitStrategy	自旋	通过不断重试，减少切换线程导致的系统调用，而降低延迟。推荐在线程绑定到固定的CPU的场景下使用
     *         PhasedBackoffWaitStrategy	自旋 + yield + 自定义策略	CPU资源紧缺，吞吐量和延迟并不重要的场景
     *         SleepingWaitStrategy	自旋 + yield + sleep	性能和CPU资源之间有很好的折中。延迟不均匀
     *         TimeoutBlockingWaitStrategy	加锁，有超时限制	CPU资源紧缺，吞吐量和延迟并不重要的场景
     *         YieldingWaitStrategy	自旋 + yield + 自旋	性能和CPU资源之间有很好的折中。延迟比较均匀
     *         BlockingWaitStrategyDisruptor使用的默认等待策略是BlockingWaitStrategy。内部BlockingWaitStrategy使用典型的锁和条件变量来处理线程唤醒。BlockingWaitStrategy是可用的等待策略中最慢的，但是在CPU使用率方面是最保守的，它将在最广泛的部署选项中提供最一致的行为。但是，再次了解已部署系统可以提高性能。

     SleepingWaitStrategy 尝试通过使用简单的繁忙等待循环来保守CPU使用率像BlockingWaitStrategy一样，SleepingWaitStrategy尝试通过使用简单的繁忙等待循环来保守CPU使用率，但在循环中间使用对LockSupport.parkNanos（1）的调用。在典型的Linux系统上，这会将线程暂停大约60µs。但是，这样做的好处是，生产线程不需要采取任何其他行动就可以增加适当的计数器，也不需要花费信号通知条件变量。但是，在生产者线程和使用者线程之间移动事件的平均等待时间会更长。它在不需要低延迟但对生产线程的影响较小的情况下效果最佳。一个常见的用例是异步日志记录。

     YieldingWaitStrategy是可在低延迟系统中使用的2种等待策略之一，在该系统中，可以选择刻录CPU周期以提高延迟。 YieldingWaitStrategy将忙于旋转以等待序列增加到适当的值。在循环体内，将调用Thread.yield()，以允许其他排队的线程运行。 当需要非常高的性能并且事件处理程序线程的数量少于逻辑核心的总数时，这是推荐的等待策略。注：启用超线程。

     BusySpinWaitStrategy是性能最高的“等待策略”，但对部署环境设置了最高的约束。 仅当事件处理程序线程的数量小于包装盒上的物理核心数量时，才应使用此等待策略。 注：超线程应该被禁用。

     BlockingWaitStrategy是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
     SleepingWaitStrategy的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景
     YieldingWaitStrategy的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于CPU逻辑核心数的场景中，推荐使用此策略;例如，CPU开启超线程的特性
     */
    void start() {
        disruptor = new Disruptor<EventThread.FunEvent>(
                EventThread.FunEvent::new,
                RINGBUFFER_SIZE,
                ThreadPoolUtil.getFactory("E"),
                ProducerType.MULTI,
                new BlockingWaitStrategy() //额外消耗最低
        );
        ringBuffer = disruptor.getRingBuffer()
        def consumers = range(CONSUMER_SIZE).mapToObj(f -> ts.get(count++ % ts.size())).collect(Collectors.toList())
        disruptor.handleEventsWithWorkerPool(consumers as EventThread<F>[])
        disruptor.start()
        fs.start()
    }


    /**
     * 中止
     * @return
     */
    def stop() {
        key = false
        fs.stop()
        disruptor.shutdown()
        logger.info("Disruptor关闭了!")
    }

    /**
     * 生产
     * @param produce
     */
    def send(Closure<F> produce) {
        if (produce() == null) FailException.fail("生产者类型错误")
        if (ThreadPoolUtil.getFunPool().activeCount > POOL_SIZE - 5) {
            logger.warn("生产者线程繁忙,丢弃改任务")
            return
        }
        def index = -fs.getStart() - 1
        while (key) {
            def qps = fs.getQps()
            logger.info("当前设计QPS:{},实际QPS:{}", qps, disruptor.getCursor() - index)
            if (qps == 0) continue
            if (qps < 0) break
            fun {
                qps.times {
                    send(produce())
                }
            }
            index = disruptor.getCursor()
            sleep(1.0)
        }
        stop()
    }

    /**
     * 生产对象
     * @param f
     * @return
     */
    def send(F f) {
        if (key) {
            ringBuffer.publishEvent {e, s ->
                {
                    e.setEvent(f)
                }
            }
        }
    }

}
