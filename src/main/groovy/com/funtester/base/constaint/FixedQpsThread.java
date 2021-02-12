package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.config.HttpClientConstant;
import com.funtester.frame.execute.FixedQpsConcurrent;
import com.funtester.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FixedQpsThread<T> extends ThreadBase<T> {

    private static Logger logger = LoggerFactory.getLogger(FixedQpsThread.class);

    public int qps;

    /**
     * 根据属性isTimesMode判断,次数或者时间(单位ms)
     */
    public int limit;

    public boolean isTimesMode;

    public FixedQpsThread(T t, int limit, int qps, MarkThread markThread, boolean isTimesMode) {
        this.limit = limit;
        this.qps = qps;
        this.mark = markThread;
        this.t = t;
        this.isTimesMode = isTimesMode;
    }


    protected FixedQpsThread() {
        super();
    }

    @Override
    public void run() {
        try {
            before();
            threadmark = this.mark == null ? EMPTY : this.mark.mark(this);
            FixedQpsConcurrent.executeTimes.getAndIncrement();
            long s = Time.getTimeStamp();
            doing();
            long e = Time.getTimeStamp();
            long diff = e - s;
            FixedQpsConcurrent.allTimes.add(diff);
            if (diff > HttpClientConstant.MAX_ACCEPT_TIME)
                FixedQpsConcurrent.marks.add(diff + CONNECTOR + threadmark + CONNECTOR + Time.getNow());
        } catch (Exception e) {
            FixedQpsConcurrent.errorTimes.getAndIncrement();
            logger.warn("执行任务失败！,标记:{}", threadmark, e);
        } finally {
            after();
        }
    }

    @Override
    public void before() {

    }

    /**
     * 子类必需实现改方法,不然调用deepclone方法会报错
     *
     * @return
     */
    public abstract FixedQpsThread clone();


}
