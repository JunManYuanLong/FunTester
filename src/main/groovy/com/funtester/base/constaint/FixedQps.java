package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.execute.FixedQpsConcurrent;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FixedQps<F> extends ThreadBase<F> {

    private static Logger logger = LogManager.getLogger(FixedQps.class);

    public int qps;

    /**
     * 此处的limit指的是总的QPS限制量
     *
     * @param f
     * @param limit
     * @param qps
     * @param markThread
     * @param isTimesMode
     */
    public FixedQps(F f, int limit, int qps, MarkThread markThread, boolean isTimesMode) {
        this.limit = limit;
        this.qps = qps;
        this.mark = markThread;
        this.f = f;
        this.isTimesMode = isTimesMode;
    }

    protected FixedQps() {
        super();
    }

    @Override
    public void run() {
        try {
            long s = Time.getTimeStamp();
            doing();
            count(s);
        } catch (Exception e) {
            FixedQpsConcurrent.errorTimes.increment();
            logger.warn("执行任务失败！", e);
        }
    }

    @Override
    public void count(long s) {
        if (COUNT) FixedQpsConcurrent.allTimes.add((short) (Time.getTimeStamp() - s));
        if (INTERCEPT) interceptCosts.add((short) (Time.getTimeStamp() - s));
    }

}
