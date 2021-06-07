package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.execute.FixedQpsConcurrent;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FixedQps<F> extends ThreadBase<F> {

    private static Logger logger = LogManager.getLogger(FixedQps.class);

    public int qps;

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
            long e = Time.getTimeStamp();
            int diff = (int) (e - s);
            FixedQpsConcurrent.allTimes.add(diff);
        } catch (Exception e) {
            FixedQpsConcurrent.errorTimes.getAndIncrement();
            logger.warn("执行任务失败！", e);
        }
    }

}
