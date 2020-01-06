package com.fun.frame.thead;

import com.fun.base.constaint.ThreadLimitTimesCount;
import com.fun.base.interfaces.MarkRequest;
import com.fun.config.Constant;
import com.fun.config.HttpClientConstant;
import com.fun.frame.Save;
import com.fun.frame.excute.Concurrent;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.FunRequest;
import com.fun.frame.httpclient.GCThread;
import com.fun.utils.Time;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * http请求多线程类
 */
public class RequestThreadTimes extends ThreadLimitTimesCount {

    private static final long serialVersionUID = -2751325651625435070L;

    static Logger logger = LoggerFactory.getLogger(RequestThreadTimes.class);

    /**
     * 记录总的请求超时的情况
     */
    public static Vector<String> requestMark = new Vector<>();

    /**
     * 请求
     */
    public HttpRequestBase request;

    /**
     * 标记对象
     */
    public MarkRequest mark;

    /**
     * 记录当前线程超时请求
     */
    public List<String> marks = new ArrayList<>();

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param times   每个线程运行的次数
     */
    public RequestThreadTimes(HttpRequestBase request, int times) {
        this.request = request;
        this.times = times;
        this.mark = new MarkRequest() {
            @Override
            public String mark(HttpRequestBase base) {
                return EMPTY;
            }
        };
    }

    /**
     * 应对对每个请求进行标记的情况
     *
     * @param request
     * @param times
     * @param mark
     */
    public RequestThreadTimes(HttpRequestBase request, int times, MarkRequest mark) {
        this(request, times);
        this.mark = mark;
    }

    protected RequestThreadTimes() {
        super();
    }

    @Override
    public void before() {
        super.before();
        GCThread.starts();
    }

    /**
     * @throws Exception
     */
    @Override
    protected void doing() throws Exception {
        FanLibrary.excuteSimlple(request);
    }

    @Override
    protected void after() {
        requestMark.addAll(marks);
        GCThread.stop();
        synchronized (RequestThreadTimes.class) {
            if (countDownLatch.getCount() == 0) Save.saveStringList(requestMark, Constant.DEFAULT_STRING);
        }
    }

    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = Time.getTimeStamp();
            for (int i = 0; i < times; i++) {
                try {
                    String m = this.mark.mark(request);
                    long s = Time.getTimeStamp();
                    doing();
                    long e = Time.getTimeStamp();
                    long diff = e - s;
                    t.add(diff);
                    if (diff > HttpClientConstant.MAX_ACCEPT_TIME) marks.add(diff + CONNECTOR + m);
                    excuteNum++;
                    if (status()) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
                    errorNum++;
                }
            }
            long ee = Time.getTimeStamp();
            logger.info("执行次数：{}，错误次数: {},总耗时：{} s", times, errorNum, (ee - ss) / 1000 + 1);
            Concurrent.allTimes.addAll(t);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            if (countDownLatch != null)
                countDownLatch.countDown();
            after();
        }

    }

    @Override
    public RequestThreadTimes clone() {
        RequestThreadTimes threadTimes = new RequestThreadTimes();
        threadTimes.times = this.times;
        threadTimes.request = FunRequest.cloneRequest(request);
        threadTimes.mark = this.mark;
        return threadTimes;
    }

}
