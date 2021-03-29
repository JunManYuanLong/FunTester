package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.config.HttpClientConstant;
import com.funtester.frame.execute.Concurrent;
import com.funtester.httpclient.GCThread;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public abstract class ThreadLimitTimeCount<F> extends ThreadBase<F> {

    private static final long serialVersionUID = -7017995186493855741L;

    private static final Logger logger = LogManager.getLogger(ThreadLimitTimeCount.class);

    public List<String> marks = new ArrayList<>();

    /**
     * 任务请求执行时间,单位是ms秒
     */
    public int time;

    public ThreadLimitTimeCount(F f, int time, MarkThread markThread) {
        this.time = time * 1000;
        this.f = f;
        this.mark = markThread;
    }

    protected ThreadLimitTimeCount() {
        super();
    }

    @Override
    public void run() {
        try {
            before();
            long ss = Time.getTimeStamp();
            while (true) {
                try {
                    threadmark = mark == null ? EMPTY : this.mark.mark(this);
                    long s = Time.getTimeStamp();
                    doing();
                    long et = Time.getTimeStamp();
                    executeNum++;
                    int diff =(int) (et - s);
                    costs.add(diff);
                    if (diff > HttpClientConstant.MAX_ACCEPT_TIME)
                        marks.add(diff + CONNECTOR + threadmark + CONNECTOR + Time.getNow());
                    if ((et - ss) > time || status() || ThreadBase.needAbort()) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
                    logger.warn("执行失败对象的标记:{}", threadmark);
                    errorNum++;
                }
            }
            long ee = Time.getTimeStamp();
            logger.info("线程:{},执行次数：{}, 失败次数: {},总耗时: {} s", threadName, executeNum, errorNum, (ee - ss) / 1000.0);
            Concurrent.allTimes.addAll(costs);
            Concurrent.requestMark.addAll(marks);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            after();
        }

    }



    public boolean status() {
        return errorNum > 10;
    }

    /**
     * 运行待测方法的之前的准备
     */
    @Override
    public void before() {
        super.before();
    }

    @Override
    protected void after() {
        super.after();
        GCThread.stop();
    }


}
