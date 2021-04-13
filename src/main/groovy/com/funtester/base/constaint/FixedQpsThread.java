package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.execute.FixedQpsConcurrent;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FixedQpsThread<F> extends ThreadBase<F> {

    private static Logger logger = LogManager.getLogger(FixedQpsThread.class);

    public int qps;

    /**
     * 根据属性isTimesMode判断,次数或者时间(单位ms)
     */
    public int limit;

    public boolean isTimesMode;

    public FixedQpsThread(F f, int limit, int qps, MarkThread markThread, boolean isTimesMode) {
        this.limit = limit;
        this.qps = qps;
        this.mark = markThread;
        this.f = f;
        this.isTimesMode = isTimesMode;
    }


    protected FixedQpsThread() {
        super();
    }

    @Override
    public void run() {
        try {
            long s = Time.getTimeStamp();
            doing();
            long e = Time.getTimeStamp();
            FixedQpsConcurrent.executeTimes.getAndIncrement();
            FixedQpsConcurrent.allTimes.add((int) (e - s));
        } catch (Exception e) {
            logger.warn("任务执行失败!", e);
            FixedQpsConcurrent.errorTimes.getAndIncrement();
        }
    }

}
