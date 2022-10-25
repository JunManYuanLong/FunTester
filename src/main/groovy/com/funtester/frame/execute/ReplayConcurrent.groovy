package com.funtester.frame.execute

import com.funtester.base.bean.AbstractBean
import com.funtester.frame.SourceCode
import com.funtester.utils.LogUtil
import com.funtester.utils.RWUtil
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.LongAdder

/**
 * 回放功能执行类*/
class ReplayConcurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(ReplayConcurrent.class);

    static ThreadPoolExecutor executor

    static boolean key = true

    static int MAX_LENGTH = 800000

    int threadNum = 2

    String name

    String fileName

    int multiple

    Closure handle

    List<ReplayLog> logs

    DelayQueue<ReplayLog> logDelayQueue = new DelayQueue<ReplayLog>()

    LongAdder total = new LongAdder()

    ReplayConcurrent(String name, String fileName, int multiple, Closure handle) {
        this.name = name
        this.fileName = fileName
        this.multiple = multiple
        this.handle = handle

    }

    void start() {
        if (executor == null) executor = ThreadPoolUtil.createCachePool(THREADPOOL_MAX, "R")
        time({
            RWUtil.readFile(fileName, {
                def delay = new ReplayLog(it)
                if (delay.getTimestamp() != 0) logDelayQueue.add(delay)
            })
        }, "读取日志$fileName")
        logs = logDelayQueue.toList()
        def timestamp = logs.get(0).getTimestamp()
        logDelayQueue.clear()
        AtomicInteger index = new AtomicInteger()
        AtomicInteger size = new AtomicInteger()
        def LogSize = logs.size()
        AtomicInteger diff = new AtomicInteger()
        threadNum.times {
            fun {
                while (key) {
                    if (index.get() % LogSize == 0) diff.set(getMark() - timestamp)
                    if (index.get() % MAX_LENGTH == 0) size.set(logDelayQueue.size())
                    if (size.get() > MAX_LENGTH) {
                        sleep(1.0)
                        size.set(logDelayQueue.size())
                    }
                    def replay = logs.get(index.getAndIncrement() % LogSize)
                    logDelayQueue.add(replay.clone(replay.timestamp + diff.get()))
                }
            }
        }
        threadNum.times {
            fun {
                while (key) {
                    def poll = logDelayQueue.poll(1, TimeUnit.SECONDS)
                    if (poll != null) {
                        executor.execute {
                            multiple.times {
                                handle(poll.getUrl())
                                total.add(1)
                            }
                        }

                    }
                }
            }
        }
        fun {
            while (key) {
                sleep(COUNT_INTERVAL as double)
                int real = total.sumThenReset() / COUNT_INTERVAL as int
                def active = executor.getActiveCount()
                def count = active == 0 ? 1 : active
                logger.info("{} ,实际QPS:{} 活跃线程数:{} 单线程效率:{}", name, real, active, real / count as int)
            }
        }

    }

    /**
     * 中止
     * @return
     */
    def stop() {
        key = false
        if (executor != null && !executor.isShutdown()) executor.shutdown()
        logger.info("replay压测关闭了!")
    }

    /**
     * 日志对象*/
    static class ReplayLog extends AbstractBean implements Delayed {

        int timestamp

        String url

        ReplayLog(String logLine) {
            def log = LogUtil.getLog(logLine)
            this.url = log.getUrl()
            this.timestamp = log.getTime()
        }

        ReplayLog(int timestamp, String url) {
            this.timestamp = timestamp
            this.url = url
        }

        @Override
        long getDelay(TimeUnit unit) {
            return this.timestamp - getMark()
        }

        @Override
        int compareTo(Delayed o) {
            return this.timestamp - o.timestamp
        }

        protected Object clone(int timestamp) {
            return new ReplayLog(timestamp, this.url)
        }
    }
}
