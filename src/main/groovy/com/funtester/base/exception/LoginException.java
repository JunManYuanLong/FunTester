package com.funtester.base.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * 处理项目中的登录异常
 */
public class LoginException extends FailException {

    private static final long serialVersionUID = 8674617502387938483L;

    private LoginException() {
        super();
    }

    private LoginException(String message) {
        super(message);
    }

    public static void fail(String message) {
        throw new LoginException(message);
    }

    /**
     * 用于处理记录登录响应结果的抛异常方法
     *
     * @param response 登录接口响应结果
     */
    public static void fail(JSONObject response) {
        fail(response.toJSONString());
    }


}
