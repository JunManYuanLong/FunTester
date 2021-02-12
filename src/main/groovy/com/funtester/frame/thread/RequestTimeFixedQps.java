package com.funtester.frame.thread;

import com.funtester.base.constaint.FixedQpsThread;
import com.funtester.base.interfaces.MarkRequest;
import com.funtester.frame.httpclient.FanLibrary;
import com.funtester.frame.httpclient.FunRequest;
import com.funtester.frame.httpclient.GCThread;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressFBWarnings("CN_IMPLEMENTS_CLONE_BUT_NOT_CLONEABLE")
public class RequestTimeFixedQps<T extends HttpRequestBase> extends FixedQpsThread<HttpRequestBase> {

    private static Logger logger = LoggerFactory.getLogger(RequestTimeFixedQps.class);

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
        FanLibrary.executeSimlple(t);
    }

    @Override
    public RequestTimeFixedQps clone() {
        RequestTimeFixedQps newone = new RequestTimeFixedQps();
        newone.t = FunRequest.cloneRequest(this.t);
        newone.mark = this.mark == null ? null : this.mark.clone();
        newone.qps = this.qps;
        newone.isTimesMode = this.isTimesMode;
        newone.limit = this.limit;
        return newone;
    }


}
