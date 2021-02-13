package com.funtester.base.exception;

import com.alibaba.fastjson.JSONObject;
import com.funtester.frame.SourceCode;
import com.funtester.httpclient.FunRequest;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * 用于处理验证过程中的异常
 */
public class VerifyException extends FailException {

    private static final long serialVersionUID = 7916010541762451964L;

    private VerifyException() {
        super();
    }

    private VerifyException(HttpRequestBase request) {
        super(request.toString());
    }

    private VerifyException(String message) {
        super(message);
    }


    public static void fail(String message) {
        SourceCode.getiMessage().sendBusinessMessage();
        throw new VerifyException(message);
    }

    public static void fail(JSONObject message) {
        SourceCode.getiMessage().sendBusinessMessage();
        fail(message.toJSONString());
    }

    public static void fail(HttpRequestBase request) {
        SourceCode.getiMessage().sendBusinessMessage();
        fail(FunRequest.initFromRequest(request).toString());
    }


}
