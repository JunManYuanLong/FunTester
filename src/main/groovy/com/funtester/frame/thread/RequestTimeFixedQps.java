package com.funtester.frame.thread;

import com.funtester.base.constaint.FixedQpsThread;
import com.funtester.base.interfaces.MarkRequest;
import com.funtester.httpclient.FunLibrary;
import com.funtester.httpclient.FunRequest;
import com.funtester.httpclient.GCThread;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestTimeFixedQps extends FixedQpsThread<HttpRequestBase> {

    private static final long serialVersionUID = -64206522585960792L;

    private static Logger logger = LogManager.getLogger(RequestTimeFixedQps.class);

    private RequestTimeFixedQps() {

    }

    public RequestTimeFixedQps(int qps, int time, MarkRequest markRequest, HttpRequestBase request) {
        super(request, time, qps, markRequest, false);
    }

    @Override
    public void before() {
        super.before();
        GCThread.starts();
    }

    @Override
    protected void doing() throws Exception {
        FunLibrary.executeSimlple(f);
    }

    @Override
    public RequestTimeFixedQps clone() {
        RequestTimeFixedQps newone = new RequestTimeFixedQps();
        newone.f = FunRequest.cloneRequest(this.f);
        newone.mark = this.mark == null ? null : this.mark.clone();
        newone.qps = this.qps;
        newone.isTimesMode = this.isTimesMode;
        newone.limit = this.limit;
        return newone;
    }


}
