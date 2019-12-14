package com.fun.base.constaint;

import com.fun.frame.excute.Concurrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.fun.utils.Time.getTimeStamp;

/**
 * 请求时间限制的多线程类,限制每个线程执行的次数
 *
 * <p>
 * 通常在测试某项用例固定时间的场景下使用,可以提前终止测试用例
 * </p>
 *
 * @param <T> 闭包参数传递使用,Groovy脚本会有一些兼容问题,部分对象需要tostring获取参数值
 */
public abstract class ThreadLimitTimes<T> extends ThreadBase {

    private static final Logger logger = LoggerFactory.getLogger(ThreadLimitTimes.class);

    private static final long serialVersionUID = -8338362913850019273L;

    /**
     * 全局的时间终止开关
     */
    private static boolean key = false;

    /**
     * 任务请求执行次数
     */
    public int times;

    /**
     * 用于设置访问资源
     */
    public T t;

    public ThreadLimitTimes(T t, int times) {
        this(times);
        this.t = t;
    }

    public ThreadLimitTimes(int times) {
        this();
        this.times = times;
    }

    protected ThreadLimitTimes() {
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
                excuteNum++;
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
    @Override
    public void before() {
        key = false;
    }
}
