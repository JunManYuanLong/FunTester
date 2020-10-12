package com.fun.frame.thead

import com.fun.base.constaint.ThreadBase
import com.fun.base.exception.ParamException
import com.fun.base.interfaces.MarkRequest
import com.fun.frame.SourceCode
import org.apache.http.client.methods.HttpRequestBase

import java.util.concurrent.atomic.AtomicInteger

class FixedQpsHeaderMark extends SourceCode implements MarkRequest, Cloneable, Serializable {

    private static final long serialVersionUID = -158942567078477L;

    public static AtomicInteger num = new AtomicInteger(10000);

    String headerName = DEFAULT_STRING;

    @Override
    public String mark(ThreadBase threadBase) {
        if (threadBase instanceof RequestThreadTime) {
            RequestThreadTime req = (RequestThreadTime) threadBase;
            return mark(req.request);
        } else if (threadBase instanceof RequestThreadTimes) {
            RequestThreadTimes req = (RequestThreadTimes) threadBase;
            return mark(req.request);
        } else {
            ParamException.fail(threadBase.getClass().toString());
        }
        return EMPTY;
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
        return value;
    }

    @Override
    public HeaderMark clone() {
        return new HeaderMark(headerName);
    }

    public HeaderMark(String headerName) {
        this.headerName = headerName;
    }

    public HeaderMark() {

    }

}