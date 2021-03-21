package com.funtester.frame.thread

import com.funtester.base.constaint.ThreadBase
import com.funtester.base.exception.ParamException
import com.funtester.base.interfaces.MarkRequest
import com.funtester.frame.SourceCode
import org.apache.http.client.methods.HttpRequestBase

import java.util.concurrent.atomic.AtomicInteger

/**
 * 针对固定QPS模式的多线程对象的标记类
 */
class FixedQpsHeaderMark extends SourceCode implements MarkRequest, Cloneable, Serializable {

    private static final long serialVersionUID = -158942567078477L;

    private static volatile AtomicInteger num = new AtomicInteger(10000);

    String headerName;

    @Override
    String mark(ThreadBase threadBase) {
        if (threadBase instanceof RequestTimesFixedQps) {
            RequestTimesFixedQps req = (RequestTimesFixedQps) threadBase;
            return mark(req.t);
        } else if (threadBase instanceof RequestTimeFixedQps) {
            RequestThreadTimes req = (RequestTimeFixedQps) threadBase;
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
    String mark(HttpRequestBase base) {
        base.removeHeaders(headerName);
        String value = 8 + EMPTY + num.getAndIncrement();
        base.addHeader(headerName, value);
        value;
    }

    @Override
    FixedQpsHeaderMark clone() {
        new FixedQpsHeaderMark(headerName);
    }

    FixedQpsHeaderMark(String headerName) {
        this.headerName = headerName;
    }

    private FixedQpsHeaderMark() {

    }

}