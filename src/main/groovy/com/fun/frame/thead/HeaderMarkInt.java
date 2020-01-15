package com.fun.frame.thead;

import com.fun.base.interfaces.MarkRequest;
import com.fun.frame.SourceCode;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.Serializable;

public class HeaderMarkInt extends SourceCode implements MarkRequest, Cloneable, Serializable {

    private static final long serialVersionUID = -1595942567071153477L;

    String headerName;

    int i;

    int num = 100_0000;

    @Override
    public String mark(HttpRequestBase request) {
        request.removeHeaders(headerName);
        i = i == 0 ? getRandomInt(8999) + 1000 : i;
        String value = 8 + EMPTY + i + num++;
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
