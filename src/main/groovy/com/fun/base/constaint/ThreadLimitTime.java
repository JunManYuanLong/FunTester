package com.fun.base.constaint;

import com.fun.frame.excute.Concurrent;
import com.fun.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求时间限制的多线程类,限制每个线程执行的时间
 * <p>
 * 通常在测试某项用例固定时间的场景下使用,可以提前终止测试用例
 * </p>
 *
 * @param <T> 闭包参数传递使用,Groovy脚本会有一些兼容问题,部分对象需要tostring获取参数值
 */
public abstract class ThreadLimitTime<T> extends ThreadBase {

    private static final Logger logger = LoggerFactory.getLogger(ThreadLimitTime.class);

    private static final long serialVersionUID = 870634253961955770L;

    /**
     * 全局的时间终止开关
     */
    private static boolean key = false;

    /**
     * 任务请求执行时间,单位是秒
     */
    public int time;

    /**
     * 用于设置访问资源
     */
    public T t;

    public ThreadLimitTime(T t, int time) {
        this(time);
        this.t = t;
    }

    public ThreadLimitTime(int time) {
        this();
        this.time = time * 1000;
    }

    protected ThreadLimitTime() {
    }

    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = Time.getTimeStamp();
            while (true) {
                long s = Time.getTimeStamp();
                doing();
                long e = Time.getTimeStamp();
                t.add(e - s);
                excuteNum++;
                if ((e - ss) > time || key) break;
            }
            long ee = Time.getTimeStamp();
            logger.info("执行时间：{} s，总耗时：{}", time / 1000, ee - ss);
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
     * 用于在某些情况下提前终止测试
     */
    public static void stopAllThread() {
        key = true;
    }

    /**
     * 运行待测方法的之前的准备
     */
    @Override
    public void before() {
        key = false;
    }


}
