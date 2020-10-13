package com.fun.frame.thead;

import com.fun.base.constaint.FixedQpsThread;
import com.fun.base.interfaces.MarkRequest;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.FunRequest;
import com.fun.frame.httpclient.GCThread;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestTimesFixedQps extends FixedQpsThread {

    private static Logger logger = LoggerFactory.getLogger(RequestTimesFixedQps.class);

    public HttpRequestBase request;

    private RequestTimesFixedQps() {

    }

    public RequestTimesFixedQps(int qps, int times, MarkRequest markRequest, HttpRequestBase request) {
        this.qps = qps;
        this.limit = times;
        this.isTimesMode = true;
        this.mark = markRequest;
        this.request = request;
    }

    @Override
    public void before() {
        super.before();
        GCThread.starts();
    }

    @Override
    protected void doing() throws Exception {
        FanLibrary.executeSimlple(request);
    }

    @Override
    public FixedQpsThread clone() {
        RequestTimesFixedQps newone = new RequestTimesFixedQps();
        newone.request = FunRequest.cloneRequest(this.request);
        newone.mark = this.mark == null ? null : this.mark.clone();
        newone.qps = this.qps;
        newone.isTimesMode = this.isTimesMode;
        newone.limit = this.limit;
        return newone;
    }


}
