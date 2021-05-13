package com.funtester.base.constaint;

import com.funtester.frame.execute.Concurrent;
import com.funtester.httpclient.GCThread;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 为了适应OK项目,新增类,后续{@link ThreadLimitTimeCount}和{@link ThreadLimitTimesCount}将会在某个时刻被弃用,会一直保留兼容旧用例
 */
public abstract class LimitThread<F> extends ThreadBase<F> {

    private static final long serialVersionUID = -4617192188292407063L;

    private static final Logger logger = LogManager.getLogger(ThreadLimitTimesCount.class);

    public LimitThread(F f, int times, boolean isTimesMode) {
        this.isTimesMode = isTimesMode;
        this.limit = times;
        this.f = f;
    }

    protected LimitThread() {
        super();
    }

    @Override
    public void run() {
        try {
            before();
            long ss = Time.getTimeStamp();
            int times = 0;
            while (true) {
                try {
                    long s = Time.getTimeStamp();
                    doing();
                    long e = Time.getTimeStamp();
                    executeNum++;
                    int diff = (int) (e - s);
                    costs.add(diff);
                    if ((isTimesMode ? executeNum >= limit : (e - ss) >= limit) || ThreadBase.needAbort()) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
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
