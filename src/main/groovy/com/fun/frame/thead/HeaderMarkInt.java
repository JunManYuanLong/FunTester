package com.fun.frame.thead;

import com.fun.base.interfaces.MarkRequest;
import com.fun.frame.SourceCode;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.Serializable;

public class HeaderMarkInt extends SourceCode implements MarkRequest,Cloneable, Serializable {

    private static final long serialVersionUID = -1595942567071153477L;

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
        return new HeaderMarkInt(headerName);
    }

    public HeaderMarkInt(String headerName) {
        this.headerName = headerName;
    }


}
