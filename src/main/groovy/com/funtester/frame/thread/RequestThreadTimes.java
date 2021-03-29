package com.funtester.frame.thread;

import com.funtester.base.constaint.ThreadLimitTimesCount;
import com.funtester.base.interfaces.MarkThread;
import com.funtester.httpclient.FunLibrary;
import com.funtester.httpclient.FunRequest;
import com.funtester.httpclient.GCThread;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http请求多线程类
 */
public class RequestThreadTimes extends ThreadLimitTimesCount<HttpRequestBase> {

    private static final long serialVersionUID = 84690314667174004L;

    private static Logger logger = LogManager.getLogger(RequestThreadTimes.class);

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param times   每个线程运行的次数
     */
    public RequestThreadTimes(HttpRequestBase request, int times) {
        super(request, times, null);
    }

    /**
     * 应对对每个请求进行标记的情况
     *
     * @param request
     * @param times
     * @param mark
     */
    public RequestThreadTimes(HttpRequestBase request, int times, MarkThread mark) {
        super(request, times, mark);
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
        FunLibrary.executeSimlple(f);
    }

    @Override
    public RequestThreadTimes clone() {
        RequestThreadTimes threadTimes = new RequestThreadTimes();
        threadTimes.times = this.times;
        threadTimes.f = FunRequest.cloneRequest(f);
        threadTimes.mark = mark == null ? null : mark.clone();
        return threadTimes;
    }


}
