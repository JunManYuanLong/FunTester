package com.funtester.base.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * 参数错误运行异常类
 */
public class ParamException extends FailException {

    private static final long serialVersionUID = -5079364420579956243L;

    private ParamException() {
        super();
    }

    private ParamException(String message) {
        super(message);
    }

    public static void fail(String message) {
        throw new ParamException(message);
    }

    public static void fail(JSONObject message) {
        throw new ParamException(message.toJSONString());
    }


}
