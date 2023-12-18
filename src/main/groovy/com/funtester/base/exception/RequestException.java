package com.funtester.base.exception;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

/**
 * 用于处理请求异常
 */
public class RequestException extends FailException {

    private static final long serialVersionUID = 7916010541762451964L;

    private RequestException() {
        super();
    }

    private RequestException(HttpUriRequestBase request) {
        super(request.toString());
    }

    public RequestException(String message) {
        super(message);
    }

    public static void fail(HttpUriRequestBase base) {
        throw new RequestException(base);
    }

    public static void fail(String message) {
        throw new RequestException(message);
    }


}
