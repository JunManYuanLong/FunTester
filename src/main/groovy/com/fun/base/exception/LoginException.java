package com.fun.base.exception;

public class LoginException extends FailException {

    private static final long serialVersionUID = 8674617502387938483L;

    public LoginException() {
        super("登录异常!");
    }

    public LoginException(String name) {
        super(String.format("账号{%s}登录失败!", name));
    }

    @Override
    public void fail() {
        throw new LoginException();
    }


}
