package com.fun.base.constaint;

import com.fun.base.interfaces.MarkThread;
import com.fun.config.HttpClientConstant;
import com.fun.frame.Save;
import com.fun.frame.excute.Concurrent;
import com.fun.frame.httpclient.GCThread;
import com.fun.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 请求时间限制的多线程类,限制每个线程执行的时间
 * <p>
 * 通常在测试某项用例固定时间的场景下使用,可以提前终止测试用例
 * </p>
 *
 * @param <T> 闭包参数传递使用,Groovy脚本会有一些兼容问题,部分对象需要tostring获取参数值
 */
public abstract class ThreadLimitTimeCount<T> extends ThreadBase {

    private static final Logger logger = LoggerFactory.getLogger(ThreadLimitTimeCount.class);

    public List<String> marks = new ArrayList<>();

    public static Vector<String> requestMark = new Vector<>();

    /**
     * 全局的时间终止开关
     */
    private static boolean key = false;

    /**
     * 任务请求执行时间,单位是秒
     */
    public int time;

    public ThreadLimitTimeCount(T t, int time, MarkThread markThread) {
        this.time = time * 1000;
        this.t = t;
        this.mark = markThread;
    }

    protected ThreadLimitTimeCount() {
        super();
    }

    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = Time.getTimeStamp();
            long et = ss;
            while (true) {
                try {
                    String m = mark == null ? EMPTY : this.mark.mark(this);
                    long s = Time.getTimeStamp();
                    doing();
                    et = Time.getTimeStamp();
                    excuteNum++;
                    long diff = et - s;
                    t.add(diff);
                    output(et - ss);
                    if (diff > HttpClientConstant.MAX_ACCEPT_TIME) marks.add(diff + CONNECTOR + m);
                    excuteNum++;
                    if ((et - ss) > time || status() || key) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
                    errorNum++;
                }
            }
            long ee = Time.getTimeStamp();
            logger.info("执行次数：{}, 失败次数: {},总耗时: {} s", excuteNum, errorNum, (ee - ss) / 1000 + 1);
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

    public boolean status() {
        return errorNum > 10;
    }

    /**
     * 运行待测方法的之前的准备
     */
    @Override
    public void before() {
        key = false;
    }

    @Override
    protected void after() {
        requestMark.addAll(marks);
        GCThread.stop();
        synchronized (this.getClass()) {
            if (countDownLatch.getCount() == 0) {
                Save.saveStringList(requestMark, MARK_Path.replace(LONG_Path, EMPTY) + Time.getDate().replace(SPACE_1, CONNECTOR));
                requestMark = new Vector<>();
            }
        }
    }


}
