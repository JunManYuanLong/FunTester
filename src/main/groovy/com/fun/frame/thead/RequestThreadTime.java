package com.fun.frame.thead;

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
public class RequestThreadTime extends ThreadLimitTimeCount {

    static Logger logger = LoggerFactory.getLogger(RequestThreadTime.class);

    /**
     * 请求
     */
    public HttpRequestBase request;

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param time    每个线程运行的次数
     */
    public RequestThreadTime(HttpRequestBase request, int time) {
        super(null,time,null);
        this.request = request;
    }

    /**
     * @param request 被执行的请求
     * @param time    执行时间
     * @param mark    标记类对象
     */
    public RequestThreadTime(HttpRequestBase request, int time, MarkThread mark) {
        super(null,time, mark);
        this.request = request;
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
        FanLibrary.excuteSimlple(request);
    }


    @Override
    public RequestThreadTime clone() {
        RequestThreadTime threadTime = new RequestThreadTime();
        threadTime.time = this.time;
        threadTime.request = FunRequest.cloneRequest(request);
        threadTime.mark = mark.clone();
        return threadTime;
    }


}
