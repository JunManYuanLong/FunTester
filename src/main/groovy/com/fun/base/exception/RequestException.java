package com.fun.base.exception;

import org.apache.http.client.methods.HttpRequestBase;

public class RequestException extends FailException {

    private static final long serialVersionUID = 7916010541762451964L;

    public RequestException() {
        super("错误的请求!");
    }

    public RequestException(HttpRequestBase request) {
        super(request.toString());
    }

    public RequestException(String message) {
        super(message);
    }


}
