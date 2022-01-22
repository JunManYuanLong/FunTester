package com.funtester.frame.execute

import com.funtester.base.constaint.FunThread
import com.funtester.base.exception.FailException
import com.funtester.config.Constant
import com.funtester.frame.SourceCode
import com.funtester.frame.replay.EventThread
import com.funtester.frame.replay.FunStart
import com.funtester.utils.StringUtil
import com.lmax.disruptor.RingBuffer
import com.lmax.disruptor.YieldingWaitStrategy
import com.lmax.disruptor.dsl.Disruptor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

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
        init()
    }

    def check() {
        if (fs.getStart() % grid + fs.getRate() % grid != 0) logger.warn("参数不匹配,必须是 $grid 的整数倍")
    }

    public void init() {
        //todo:三种模型枚举类
        disruptor = new Disruptor<EventThread.FunEvent>(
                EventThread.FunEvent::new,
                16 * 16,
                ThreadPoolUtil.getFactory("D"),
                com.lmax.disruptor.dsl.ProducerType.MULTI,
                new YieldingWaitStrategy()
        );
        ringBuffer = disruptor.getRingBuffer()
        def consumers = []
        2.times {consumers << new EventThread<F>(random(ts))}
        fun {
            new FunConcurrent(consumers).start()
        }
        disruptor.handleEventsWithWorkerPool(consumers as EventThread<F>[])
        disruptor.start()
        fs.start()
    }

    public static void main(String[] args) {
        def start = new FunStart(1, 20, 1, 1, 1000, "FunTester")
        def ss = {String x ->
            {
                if (x != null) output(x)
            }
        }
        def concurrent = new EventConcurrent<String>(start, [ss])
        concurrent.send({
            StringUtil.getString(10)
        })
        FunThread.stop()

    }


    def stop() {
        fs.stop()
    }

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
