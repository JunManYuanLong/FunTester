package com.fun.frame.thread;

import com.fun.base.constaint.ThreadLimitTimeCount;
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
public class RequestThreadTime<T> extends ThreadLimitTimeCount<HttpRequestBase> {

    private static Logger logger = LoggerFactory.getLogger(RequestThreadTime.class);

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param time    每个线程运行的次数
     */
    public RequestThreadTime(HttpRequestBase request, int time) {
        super(request, time, null);
    }

    /**
     * @param request 被执行的请求
     * @param time    执行时间
     * @param mark    标记类对象
     */
    public RequestThreadTime(HttpRequestBase request, int time, MarkThread mark) {
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
        FanLibrary.executeSimlple(t);
    }


    @Override
    public RequestThreadTime clone() {
        RequestThreadTime threadTime = new RequestThreadTime();
        threadTime.time = this.time;
        threadTime.t = FunRequest.cloneRequest(t);
        threadTime.mark = mark == null ? null : mark.clone();
        return threadTime;
    }


}
