package com.funtester.frame.execute

import com.funtester.base.exception.FailException
import com.funtester.config.HttpClientConstant
import com.funtester.frame.SourceCode
import com.funtester.frame.replay.FunStart
import com.funtester.frame.replay.ReplayThread
import com.lmax.disruptor.RingBuffer
import com.lmax.disruptor.YieldingWaitStrategy
import com.lmax.disruptor.dsl.Disruptor

class FunReplay<F> extends SourceCode {

    static boolean key = true

    Disruptor<ReplayThread.FunEvent<F>> disruptor

    RingBuffer<ReplayThread.FunEvent<F>> ringBuffer

    FunStart fs

    FunReplay(FunStart fs) {
        this.fs = fs
    }


    public void init() {
        //todo:三种模型枚举类
        disruptor = new Disruptor<ReplayThread.FunEvent>(
                ReplayThread.FunEvent::new,
                1024 * 1024,
                ThreadPoolUtil.getFactory("D"),
                com.lmax.disruptor.dsl.ProducerType.MULTI,
                new YieldingWaitStrategy()
        );
        ringBuffer = disruptor.getRingBuffer()
        def consumers = []
        100.times {consumers << new ReplayThread<F>({f -> output(f)})}
        disruptor.start()
        fs.start()
    }

    def stop() {
        fs.stop()
    }

    static def stopAll() {
        key = false
    }

    def send(Closure produce) {
        def event = produce()
        if (event == null) FailException.fail("生产者类型错误")
        while (key) {
            sleep(HttpClientConstant.LOOP_INTERVAL)
            def qps = fs.getQps()
            if (qps == 0) continue
            if (qps < 0) break
            fun {
                10.times {
                    sleep(0.1)
                    (qps / 10).times {
                        send(produce())
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        output(3)
    }

    def send(F f) {
        ringBuffer.publishEvent {e, s ->
            {
                e.setEvent(f)
            }
        }
    }

}
