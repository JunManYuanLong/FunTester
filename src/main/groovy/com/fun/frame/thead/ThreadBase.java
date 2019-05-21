package com.fun.frame.thead;

import com.fun.frame.SourceCode;
import com.fun.frame.excute.Concurrent;
import com.fun.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程任务基类，可单独使用
 */
public abstract class ThreadBase extends SourceCode implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ThreadBase.class);

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    /**
     * 任务请求执行次数
     */
    public int times;

    /**
     * 计数锁
     */
    CountDownLatch countDownLatch;

    public ThreadBase() {
        super();
    }

    public ThreadBase(int times) {
        this();
        this.times = times;
    }


    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = Time.getTimeStamp();
            for (int i = 0; i < times; i++) {
                long s = Time.getTimeStamp();
                doing();
                long e = Time.getTimeStamp();
                t.add(e - s);
            }
            long ee = Time.getTimeStamp();
            logger.info("执行次数：{}，总耗时：{}", times, ee - ss);
            Concurrent.allTimes.addAll(t);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            after();
            if (countDownLatch != null)
                countDownLatch.countDown();
        }
    }

    /**
     * 运行待测方法的之前的准备
     */
    protected abstract void before();

    /**
     * 待测方法
     *
     * @throws Exception
     */
    protected abstract void doing() throws Exception;

    /**
     * 运行待测方法后的处理
     */
    protected abstract void after();


}
