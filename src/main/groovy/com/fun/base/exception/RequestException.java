package com.fun.base.exception;

import org.apache.http.client.methods.HttpRequestBase;

public class RequestException extends FailException {

    private static final long serialVersionUID = 7916010541762451964L;

    private RequestException() {
        super("错误的请求!");
    }

    private RequestException(HttpRequestBase request) {
        super(request.toString());
    }

    public RequestException(String message) {
        super(message);
    }

    public static void fail(HttpRequestBase base) {
        throw new RequestException(base);
    }

    public static void fail(String message) {
        throw new RequestException(message);
    }


}
