package com.fun.frame.thead;

import com.fun.frame.SourceCode;
import com.fun.frame.excute.Concurrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.fun.utils.Time.getTimeStamp;

/**
 * 多线程任务基类，可单独使用
 */
public abstract class ThreadBase<T> extends SourceCode implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ThreadBase.class);

    /**
     * 任务请求执行次数
     */
    public int times;

    /**
     * 计数锁
     * <p>
     * 会在concurrent类里面根据线程数自动设定
     * </p>
     */
    CountDownLatch countDownLatch;

    /**
     * 用于设置访问资源
     */
    public T t;

    public ThreadBase(T t) {
        this();
        this.t = t;
    }

    public ThreadBase() {
        super();
    }

    /**
     * groovy无法直接访问t，所以写了这个方法
     *
     * @return
     */
    public String getT() {
        return t.toString();
    }

    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = getTimeStamp();
            for (int i = 0; i < times; i++) {
                long s = getTimeStamp();
                doing();
                long e = getTimeStamp();
                t.add(e - s);
            }
            long ee = getTimeStamp();
            logger.info("执行次数：{}，总耗时：{}", times, ee - ss);
            Concurrent.allTimes.addAll(t);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            if (countDownLatch != null)
                countDownLatch.countDown();
            after();
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

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void setTimes(int times) {
        this.times = times;
    }

}
