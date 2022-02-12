package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.execute.Concurrent;
import com.funtester.httpclient.GCThread;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 请求时间限制的多线程类,限制每个线程执行的次数
 *
 * <p>
 * 通常在测试某项用例固定时间的场景下使用,可以提前终止测试用例
 * </p>
 *
 * @param <F> 闭包参数传递使用,Groovy脚本会有一些兼容问题,部分对象需要tostring获取参数值
 */
@Deprecated
public abstract class ThreadLimitTimesCount<F> extends FixedThread<F> {

    private static final long serialVersionUID = -4617192188292407063L;

    private static final Logger logger = LogManager.getLogger(ThreadLimitTimesCount.class);

    public ThreadLimitTimesCount(F f, int times, MarkThread markThread) {
        this.isTimesMode = true;
        this.limit = times;
        this.f = f;
        this.mark = markThread;
    }

    protected ThreadLimitTimesCount() {
        super();
    }

    @Override
    public void run() {
        try {
            before();
            long ss = Time.getTimeStamp();
            while (true) {
                try {
                    if (executeNum >= limit) break;
//                    threadmark = mark == null ? EMPTY : this.mark.mark(this);
                    long s = Time.getTimeStamp();
                    doing();
                    count(s);
                    executeNum++;
//                    if (diff > HttpClientConstant.MAX_ACCEPT_TIME)
//                        marks.add(diff + CONNECTOR + threadmark + CONNECTOR + Time.getNow());
                    if (ThreadBase.needAbort()) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
//                    logger.warn("执行失败对象的标记:{}", threadmark);
                    errorNum++;
                }
            }
            long ee = Time.getTimeStamp();
            if ((ee - ss) / 1000 > RUNUP_TIME + 3)
                logger.info("线程:{},执行次数：{}，错误次数: {},总耗时：{} s", threadName, executeNum, errorNum, (ee - ss) / 1000.0);
            Concurrent.allTimes.addAll(costs);
            Concurrent.requestMark.addAll(marks);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            after();
        }
    }

    @Override
    protected void after() {
        super.after();
        GCThread.stop();
    }


}
