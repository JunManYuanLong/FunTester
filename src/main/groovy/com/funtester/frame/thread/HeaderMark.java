package com.funtester.frame.thread;

import com.funtester.base.constaint.ThreadBase;
import com.funtester.base.exception.ParamException;
import com.funtester.base.interfaces.MarkRequest;
import com.funtester.frame.SourceCode;
import com.funtester.utils.StringUtil;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class HeaderMark extends SourceCode implements MarkRequest, Cloneable, Serializable {

    private static final long serialVersionUID = -1595942567071153477L;

    public static AtomicInteger threadName = new AtomicInteger(getRandomIntRange(1000, 9000));

    String headerName;

    private String m;

    int num = getRandomIntRange(100, 999) * 1000;

    @Override
    public String mark(ThreadBase threadBase) {
        if (threadBase instanceof RequestThreadTime) {
            RequestThreadTime req = (RequestThreadTime) threadBase;
            return mark(req.f);
        } else if (threadBase instanceof RequestThreadTimes) {
            RequestThreadTimes req = (RequestThreadTimes) threadBase;
            return mark(req.f);
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
        String value = m + num++;
        base.addHeader(headerName, value);
        return value;
    }

    @Override
    public HeaderMark clone() {
        return new HeaderMark(headerName);
    }

    public HeaderMark(String headerName) {
        this.headerName = headerName;
        this.m = DEFAULT_STRING + StringUtil.getStringWithoutNum(4) + threadName.getAndIncrement();
    }

    public HeaderMark() {

    }


}
