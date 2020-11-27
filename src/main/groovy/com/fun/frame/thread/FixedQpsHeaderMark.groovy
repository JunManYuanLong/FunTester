package com.fun.frame.thread

import com.fun.base.constaint.ThreadBase
import com.fun.base.exception.ParamException
import com.fun.base.interfaces.MarkRequest
import com.fun.frame.SourceCode
import org.apache.http.client.methods.HttpRequestBase

import java.util.concurrent.atomic.AtomicInteger

/**
 * 针对固定QPS模式的多线程对象的标记类
 */
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("")
class FixedQpsHeaderMark extends SourceCode implements MarkRequest, Cloneable, Serializable {

    private static final long serialVersionUID = -158942567078477L;

    public static volatile AtomicInteger num = new AtomicInteger(10000);

    String headerName;

    @Override
    public String mark(ThreadBase threadBase) {
        if (threadBase instanceof RequestTimesFixedQps) {
            RequestTimesFixedQps<HttpRequestBase> req = (RequestTimesFixedQps<HttpRequestBase>) threadBase;
            return mark(req.t);
        } else if (threadBase instanceof RequestTimeFixedQps) {
            RequestThreadTimes<HttpRequestBase> req = (RequestTimeFixedQps<HttpRequestBase>) threadBase;
            return mark(req.t);
        } else {
            ParamException.fail(threadBase.getClass().toString());
        }
        EMPTY;
    }

    /**
     * 标记请求
     *
     * @param base
     * @return
     */
    @Override
    public String mark(HttpRequestBase base) {
        base.removeHeaders(headerName);
        String value = 8 + EMPTY + num.getAndIncrement();
        base.addHeader(headerName, value);
        value;
    }

    @Override
    public FixedQpsHeaderMark clone() {
        new FixedQpsHeaderMark(headerName);
    }

    public FixedQpsHeaderMark(String headerName) {
        this.headerName = headerName;
    }

    private FixedQpsHeaderMark() {

    }

}