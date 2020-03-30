package com.fun.base.exception;

import com.alibaba.fastjson.JSONObject;
import com.fun.frame.Output;
import com.fun.frame.SourceCode;
import org.apache.http.client.methods.HttpRequestBase;

public class VerifyException extends FailException {

    private static final long serialVersionUID = 7916010541762451964L;

    private VerifyException() {
        super("验证失败!");
    }

    private VerifyException(HttpRequestBase request) {
        super(request.toString());
    }

    public VerifyException(String message) {
        super(message);
    }

    public static void fail(JSONObject response) {
        Output.output(response);
        SourceCode.getiMessage().sendBusinessMessage();
        throw new VerifyException();
    }

    public static void fail(String message) {
        SourceCode.getiMessage().sendBusinessMessage();
        throw new VerifyException(message);
    }


}
