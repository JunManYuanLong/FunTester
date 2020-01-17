package com.fun.base.interfaces;

import org.apache.http.client.methods.HttpRequestBase;

public interface MarkRequest extends MarkThread {

    public String mark(HttpRequestBase requestBase);


}
