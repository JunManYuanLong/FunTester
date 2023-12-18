package com.funtester.base.exception;

import com.alibaba.fastjson.JSONObject;
import com.funtester.httpclient.FunRequest;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

/**
 * 用于处理验证过程中的异常
 */
public class VerifyException extends FailException {

    private static final long serialVersionUID = 7916010541762451964L;

    private VerifyException() {
        super();
    }

    private VerifyException(HttpUriRequestBase request) {
        super(request.toString());
    }

    private VerifyException(String message) {
        super(message);
    }


    public static void fail(String message) {
        throw new VerifyException(message);
    }

    public static void fail(JSONObject message) {
        fail(message.toJSONString());
    }

    public static void fail(HttpUriRequestBase request) {
        fail(FunRequest.initFromRequest(request).toString());
    }


}
