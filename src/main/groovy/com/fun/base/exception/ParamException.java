package com.fun.base.exception;

public class ParamException extends FailException {

    private static final long serialVersionUID = -5079364420579956243L;

    private ParamException() {
        super("参数错误!");
    }

    private ParamException(String name) {
        super(name);
    }

    public static void fail(String name) {
        throw new ParamException(name);
    }


}
