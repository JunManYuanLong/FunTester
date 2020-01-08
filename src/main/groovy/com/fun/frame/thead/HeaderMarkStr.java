package com.fun.frame.thead;

import com.fun.base.interfaces.MarkRequest;
import com.fun.frame.SourceCode;
import com.fun.utils.RString;
import com.fun.utils.Time;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class HeaderMarkStr extends SourceCode implements MarkRequest {

    String headerName;

    String m;

    @Override
    public String mark(HttpRequestBase request) {
        request.removeHeaders(headerName);
        m = m == null ? RString.getStringWithoutNum(4) : m;
        String value = "fun_" + m + CONNECTOR + Time.getTimeStamp();
        request.addHeader(headerName, value);
        return value;
    }
    @Override
    public HeaderMarkStr clone() {
        return this;
    }
    public HeaderMarkStr(String headerName) {
        this.headerName = headerName;
    }


}
