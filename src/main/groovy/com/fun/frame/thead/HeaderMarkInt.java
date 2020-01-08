package com.fun.frame.thead;

import com.fun.base.interfaces.MarkRequest;
import com.fun.frame.SourceCode;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class HeaderMarkInt extends SourceCode implements MarkRequest {

    String headerName;

    int i;

    @Override
    public String mark(HttpRequestBase request) {
        request.removeHeaders(headerName);
        String value = "fun_" + i++;
        request.addHeader(headerName, value);
        return value;
    }

    @Override
    public HeaderMarkInt clone() {
        return this;
    }

    public HeaderMarkInt(String headerName) {
        this.headerName = headerName;
    }


}
