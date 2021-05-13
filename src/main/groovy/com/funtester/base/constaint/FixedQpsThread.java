package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.execute.FixedQpsConcurrent;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FixedQpsThread<F> extends ThreadBase<F> {

    private static Logger logger = LogManager.getLogger(FixedQpsThread.class);

    public int qps;

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
//            before();
//            threadmark = this.mark == null ? EMPTY : this.mark.mark(this);
//            FixedQpsConcurrent.executeTimes.getAndIncrement();
            long s = Time.getTimeStamp();
            doing();
            long e = Time.getTimeStamp();
            int diff = (int) (e - s);
            FixedQpsConcurrent.allTimes.add(diff);
//            if (diff > HttpClientConstant.MAX_ACCEPT_TIME)
//                FixedQpsConcurrent.marks.add(diff + CONNECTOR + threadmark + CONNECTOR + Time.getNow());
        } catch (Exception e) {
            FixedQpsConcurrent.errorTimes.getAndIncrement();
            logger.warn("执行任务失败！", e);
//            logger.warn("执行任务失败！,标记:{}", threadmark, e);
        }
    }

}
