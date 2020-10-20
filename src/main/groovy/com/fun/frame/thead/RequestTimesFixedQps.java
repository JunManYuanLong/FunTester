package com.fun.frame.thead;

import com.fun.base.constaint.FixedQpsThread;
import com.fun.base.interfaces.MarkRequest;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.FunRequest;
import com.fun.frame.httpclient.GCThread;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestTimesFixedQps<T> extends FixedQpsThread<HttpRequestBase> {

    private static Logger logger = LoggerFactory.getLogger(RequestTimesFixedQps.class);

    private RequestTimesFixedQps() {

    }

    public RequestTimesFixedQps(int qps, int times, MarkRequest markRequest, HttpRequestBase request) {
        super(request, times, qps, markRequest, true);
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
    public RequestTimesFixedQps clone() {
        RequestTimesFixedQps newone = new RequestTimesFixedQps();
        newone.t = FunRequest.cloneRequest(this.t);
        newone.mark = this.mark == null ? null : this.mark.clone();
        newone.qps = this.qps;
        newone.isTimesMode = this.isTimesMode;
        newone.limit = this.limit;
        return newone;
    }


}
