package com.funtester.frame.thread;

import com.funtester.base.constaint.ThreadLimitTimeCount;
import com.funtester.base.interfaces.MarkThread;
import com.funtester.httpclient.FunHttp;
import com.funtester.httpclient.FunRequest;
import com.funtester.httpclient.GCThread;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http请求多线程类
 */
public class RequestThreadTime extends ThreadLimitTimeCount<HttpUriRequestBase> {

    private static final long serialVersionUID = -6554503654885966097L;

    private static Logger logger = LogManager.getLogger(RequestThreadTime.class);

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param time    每个线程运行的次数
     */
    public RequestThreadTime(HttpUriRequestBase request, int time) {
        super(request, time, null);
    }

    /**
     * @param request 被执行的请求
     * @param time    执行时间
     * @param mark    标记类对象
     */
    public RequestThreadTime(HttpUriRequestBase request, int time, MarkThread mark) {
        super(request, time, mark);
    }

    protected RequestThreadTime() {
        super();
    }

    @Override
    public void before() {
        super.before();
        GCThread.starts();
    }

    @Override
    protected void doing() throws Exception {
        FunHttp.executeSimlple(f);
    }


    @Override
    public RequestThreadTime clone() {
        RequestThreadTime threadTime = new RequestThreadTime();
        threadTime.limit = this.limit;
        threadTime.f = FunRequest.cloneRequest(f);
        threadTime.mark = mark == null ? null : mark.clone();
        return threadTime;
    }


}
