package com.fun.base.constaint;

import com.fun.base.interfaces.MarkThread;
import com.fun.config.HttpClientConstant;
import com.fun.frame.excute.Concurrent;
import com.fun.frame.httpclient.GCThread;
import com.fun.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求时间限制的多线程类,限制每个线程执行的次数
 *
 * <p>
 * 通常在测试某项用例固定时间的场景下使用,可以提前终止测试用例
 * </p>
 *
 * @param <T> 闭包参数传递使用,Groovy脚本会有一些兼容问题,部分对象需要tostring获取参数值
 */
public abstract class ThreadLimitTimesCount<T> extends ThreadBase {

    private static final Logger logger = LoggerFactory.getLogger(ThreadLimitTimesCount.class);

    public List<String> marks = new ArrayList<>();

    /**
     * 全局的时间终止开关
     */
    private static boolean key = false;

    /**
     * 任务请求执行次数
     */
    public int times;

    public ThreadLimitTimesCount(T t, int times, MarkThread markThread) {
        this.times = times;
        this.t = t;
        this.mark = markThread;
    }

    protected ThreadLimitTimesCount() {
        super();
    }

    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = Time.getTimeStamp();
            for (int i = 0; i < times; i++) {
                try {
                    threadmark = mark == null ? EMPTY : this.mark.mark(this);
                    long s = Time.getTimeStamp();
                    doing();
                    long e = Time.getTimeStamp();
                    excuteNum++;
                    long diff = e - s;
                    t.add(diff);
                    if (diff > HttpClientConstant.MAX_ACCEPT_TIME) marks.add(diff + CONNECTOR + threadmark);
                    if (status() || key) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
                    errorNum++;
                }
            }
            long ee = Time.getTimeStamp();
            logger.info("执行次数：{}，错误次数: {},总耗时：{} s", times, errorNum, (ee - ss) / 1000 + 1);
            Concurrent.allTimes.addAll(t);
            Concurrent.requestMark.addAll(marks);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
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

    @Override
    public boolean status() {
        return errorNum > 10;
    }

    /**
     * 用于在某些情况下提前终止测试
     */
    public static void stopAllThread() {
        key = true;
    }

    @Override
    protected void after() {
        super.after();
        marks = new ArrayList<>();
        GCThread.stop();
    }


}
