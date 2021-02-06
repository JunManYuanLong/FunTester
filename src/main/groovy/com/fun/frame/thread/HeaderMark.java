package com.fun.frame.thread;

import com.fun.base.constaint.ThreadBase;
import com.fun.base.exception.ParamException;
import com.fun.base.interfaces.MarkRequest;
import com.fun.frame.SourceCode;
import com.fun.utils.StringUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressFBWarnings("CN_IDIOM_NO_SUPER_CALL")
public class HeaderMark extends SourceCode implements MarkRequest, Cloneable, Serializable {

    private static final long serialVersionUID = -1595942567071153477L;

    @SuppressFBWarnings("MS_SHOULD_BE_FINAL")
    public static AtomicInteger threadName = new AtomicInteger(getRandomIntRange(1000, 9000));

    String headerName;

    private String m;

    int num = getRandomIntRange(100, 999) * 1000;

    @Override
    public String mark(ThreadBase threadBase) {
        if (threadBase instanceof RequestThreadTime) {
            RequestThreadTime<HttpRequestBase> req = (RequestThreadTime<HttpRequestBase>) threadBase;
            return mark(req.t);
        } else if (threadBase instanceof RequestThreadTimes) {
            RequestThreadTimes<HttpRequestBase> req = (RequestThreadTimes<HttpRequestBase>) threadBase;
            return mark(req.t);
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
