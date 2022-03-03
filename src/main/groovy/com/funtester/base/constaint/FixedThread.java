package com.funtester.base.constaint;

import com.funtester.frame.execute.Concurrent;
import com.funtester.httpclient.GCThread;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 为了适应OK项目,新增类
 */
public abstract class FixedThread<F> extends ThreadBase<F> {

    private static final long serialVersionUID = -4617192188292407063L;

    private static final Logger logger = LogManager.getLogger(FixedThread.class);

    public FixedThread(F f, int limit, boolean isTimesMode) {
        this.isTimesMode = isTimesMode;
        this.limit = limit;
        this.f = f;
    }

    protected FixedThread() {
        super();
    }

    @Override
    public void run() {
        try {
            before();
            long ss = Time.getTimeStamp();
            while (true) {
                try {
                    executeNum++;
                    long s = Time.getTimeStamp();
                    doing();
                    count(s);
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
                    errorNum++;
                } finally {
                    if ((isTimesMode ? executeNum >= limit : (Time.getTimeStamp() - ss) >= limit) || ThreadBase.needAbort() || status())
                        break;
                }
            }
            long ee = Time.getTimeStamp();
            if ((ee - ss) / 1000 > RUNUP_TIME + 3)//区分软启动运行和正式运行
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

    @Override
    public abstract FixedThread clone();

}
