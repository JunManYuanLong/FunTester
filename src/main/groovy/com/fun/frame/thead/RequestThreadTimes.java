package com.fun.frame.thead;

import com.fun.base.constaint.ThreadLimitTimesCount;
import com.fun.base.interfaces.MarkThread;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.FunRequest;
import com.fun.frame.httpclient.GCThread;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求多线程类
 */
public class RequestThreadTimes extends ThreadLimitTimesCount {

    static Logger logger = LoggerFactory.getLogger(RequestThreadTimes.class);

    /**
     * 请求
     */
    public HttpRequestBase request;

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param times   每个线程运行的次数
     */
    public RequestThreadTimes(HttpRequestBase request, int times) {
        super(null, times, null);
        this.request = request;
    }

    /**
     * 应对对每个请求进行标记的情况
     *
     * @param request
     * @param times
     * @param mark
     */
    public RequestThreadTimes(HttpRequestBase request, int times, MarkThread mark) {
        super(null, times, mark);
        this.request = request;
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
    public RequestThreadTimes clone() {
        RequestThreadTimes threadTimes = new RequestThreadTimes();
        threadTimes.times = this.times;
        threadTimes.request = FunRequest.cloneRequest(request);
        threadTimes.mark = mark == null ? null : mark.clone();
        return threadTimes;
    }


}
