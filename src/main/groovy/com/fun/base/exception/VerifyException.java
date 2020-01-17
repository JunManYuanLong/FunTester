package com.fun.base.exception;

import com.fun.frame.Output;
import net.sf.json.JSONObject;
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
        throw new VerifyException();
    }

    public static void fail(String message) {
        throw new VerifyException(message);
    }


}
