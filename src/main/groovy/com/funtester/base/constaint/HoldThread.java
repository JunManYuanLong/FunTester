package com.funtester.base.constaint;

import com.funtester.frame.execute.HoldConcurrent;
import com.funtester.httpclient.GCThread;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 动态压力模型,增压暂停
 */
public abstract class HoldThread<F> extends ThreadBase<F> {

    private static final long serialVersionUID = -4617192188292407063L;

    private static final Logger logger = LogManager.getLogger(HoldThread.class);

    public HoldThread(F f, int limit, boolean isTimesMode) {
        this.isTimesMode = isTimesMode;
        this.limit = limit;
        this.f = f;
    }

    protected HoldThread() {
        super();
    }

    /**
     *
     */
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
            if ((ee - ss) / 1000 > RUNUP_TIME + 3)
                logger.info("线程:{},执行次数：{}，错误次数: {},总耗时：{} s", threadName, executeNum, errorNum, (ee - ss) / 1000.0);
            HoldConcurrent.allTimes.addAll(costs);
            HoldConcurrent.requestMark.addAll(marks);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            after();
        }
    }

    @Override
    public void before() {
        super.before();
        HoldConcurrent.phaser.register();
    }

    @Override
    protected void after() {
        super.after();
        GCThread.stop();
        HoldConcurrent.phaser.arriveAndDeregister();

    }


}
