package com.fun.base.exception;

public class LoginException extends FailException {

    private static final long serialVersionUID = 8674617502387938483L;

    private LoginException() {
        super("登录异常!");
    }

    private LoginException(String name) {
        super(String.format("账号{%s}登录失败!", name));
    }

    public static void fail(String name) {
        throw new LoginException(name);
    }


}
